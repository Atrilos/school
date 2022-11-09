package ru.hogwarts.shared.avatar;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@FeignClient(
        name = "avatar",
        url = "${clients.avatar.url}"
)
public interface AvatarClient {

    @GetMapping(value = "/avatar/{id}/from-db")
    ResponseEntity<byte[]> readFromDb(@PathVariable Long id);

    @GetMapping(value = "/avatar/{id}/from-file")
    ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) throws IOException;

    @GetMapping(value = "/avatar/{id}")
    ResponseEntity<Avatar> getAvatarById(@PathVariable Long id);
}
