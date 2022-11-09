package ru.hogwarts.avatar;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.shared.avatar.Avatar;
import ru.hogwarts.shared.avatar.dto.AvatarDto;
import ru.hogwarts.shared.mapper.Mapper;
;

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
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final Mapper mapper;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @SuppressWarnings("all")
    public void uploadAvatar(MultipartFile multipartFile) throws IOException {
        Avatar avatar = create(multipartFile);
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
    }

    private Avatar create(MultipartFile multipartFile) {
        Avatar avatar = Avatar.builder()
                .fileSize(multipartFile.getSize())
                .mediaType(multipartFile.getContentType())
                .build();
        return avatarRepository.save(avatar);
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

    public Avatar getAvatarById(Long avatarId) {
        return avatarRepository
                .findById(avatarId)
                .orElseThrow(() -> new EntryNotFoundException("The specified avatar not found"));
    }

    private String getExtension(String filename) {
        return filename
                .substring(filename.lastIndexOf(".") + 1);
    }

    public void deleteAvatarById(Long avatarId) {
        getAvatarById(avatarId);
        avatarRepository
                .removeAvatarById(avatarId);
    }

    public ResponseEntity<byte[]> readFromDb(Long id) {
        Avatar avatar = getAvatarById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .contentLength(avatar.getData().length)
                .body(avatar.getData());
    }

    public ResponseEntity<byte[]> readFromFs(Long id) throws IOException {
        Avatar avatar = getAvatarById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .contentLength(avatar.getFileSize())
                .body(Files.readAllBytes(Path.of(avatar.getFilePath())));
    }

    public List<AvatarDto> getPage(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository
                .findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
