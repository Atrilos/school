package ru.hogwarts.avatar;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.shared.avatar.Avatar;
import ru.hogwarts.shared.avatar.dto.AvatarDto;
import ru.hogwarts.student.model.dto.AvatarDto;
import ru.hogwarts.student.service.AvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/from-db")
    public ResponseEntity<byte[]> readFromDb(@PathVariable Long id) {
        return avatarService.readFromDb(id);
    }

    @GetMapping(value = "/{id}/from-file")
    public ResponseEntity<byte[]> readFromFs(@PathVariable Long id) throws IOException {
        return avatarService.readFromFs(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Avatar> getAvatarById(@PathVariable Long id) {
        return ResponseEntity.ok(avatarService.getAvatarById(id));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteAvatar(@PathVariable(name = "id") Long avatarId) {
        avatarService.deleteAvatarById(avatarId);
    }

    @GetMapping
    public ResponseEntity<List<AvatarDto>> getPage(@RequestParam("page") int pageNumber,
                                                   @RequestParam("size") int pageSize) {
        List<AvatarDto> list = avatarService.getPage(pageNumber, pageSize);
        return ResponseEntity.ok(list);
    }
}
