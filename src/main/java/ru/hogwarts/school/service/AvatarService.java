package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.EntryNotFoundException;
import ru.hogwarts.school.mapper.Mapper;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.dto.AvatarDto;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    private final Mapper mapper;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @SuppressWarnings("all")
    public void uploadAvatar(Long id, MultipartFile multipartFile) throws IOException {
        log.info("Uploading new avatar");
        Avatar avatar = create(id, multipartFile);
        Path filePath = Path.of(avatarsDir, avatar.getId() + "." + getExtension(multipartFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = multipartFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }
        avatar.setData(generateImagePreview(filePath));
        avatar.setFilePath(filePath.toString());
        avatarRepository.save(avatar);
    }

    private Avatar create(Long id, MultipartFile multipartFile) {
        Student student = studentService.getStudentById(id);
        return Avatar.builder()
                .fileSize(multipartFile.getSize())
                .mediaType(multipartFile.getContentType())
                .student(student)
                .build();
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

    private String getExtension(String filename) {
        return filename
                .substring(filename.lastIndexOf(".") + 1);
    }

    public void deleteAvatarById(Long id) {
        getAvatarById(id);
        avatarRepository
                .removeAvatarById(id);
    }

    public ResponseEntity<byte[]> readFromDb(Long id) {
        log.info("Get avatar from database with id={}", id);
        Avatar avatar = getAvatarById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .contentLength(avatar.getData().length)
                .body(avatar.getData());
    }

    public ResponseEntity<byte[]> readFromFs(Long id) throws IOException {
        log.info("Get avatar from drive with id={}", id);
        Avatar avatar = getAvatarById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .contentLength(avatar.getFileSize())
                .body(Files.readAllBytes(Path.of(avatar.getFilePath())));
    }

    public List<AvatarDto> getPage(int pageNumber, int pageSize) {
        log.info("Get avatars as page from page number {} with page size {}",
                pageNumber, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository
                .findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    protected Avatar getAvatarById(Long id) {
        return avatarRepository
                .findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Avatar with id=" + id + " doesn't exist",
                        "The specified avatar not found"));
    }
}
