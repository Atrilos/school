package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
@RequiredArgsConstructor
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @SuppressWarnings("all")
    public void uploadAvatar(Long studentId, MultipartFile avatar) throws IOException {
        Student student = studentService.getStudentById(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtension(avatar.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = avatar.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }
        Avatar foundAvatar = findAvatarByStudentId(studentId);
        foundAvatar.setStudent(student);
        foundAvatar.setFilePath(filePath.toString());
        foundAvatar.setFileSize(avatar.getSize());
        foundAvatar.setMediaType(avatar.getContentType());
        foundAvatar.setData(generateImagePreview(filePath));
        avatarRepository.save(foundAvatar);
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            double scale = 100d / image.getWidth();
            int height = image.getWidth() > 100 ?
                    (int) (image.getHeight() * scale) : image.getHeight();
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    public Avatar findAvatarByStudentId(Long studentId) {
        Optional<Avatar> foundAvatar = avatarRepository.findByStudentId(studentId);
        return foundAvatar.orElse(new Avatar());
    }

    public Avatar findAvatarById(Long avatarId) {
        return avatarRepository.findById(avatarId).orElseThrow(() -> new EntryNotFoundException("The specified avatar not found"));
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public void deleteAvatarById(Long avatarId) {
        avatarRepository.removeAvatarById(avatarId);
    }
}
