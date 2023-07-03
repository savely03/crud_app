package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    @GetMapping("/{id}/db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@PathVariable Long id) {
        Pair<byte[], String> pair = avatarService.findAvatarByStudentIdFromDb(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .contentLength(pair.getFirst().length)
                .body(pair.getFirst());

    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Pair<byte[], String> pair = avatarService.findAvatarByStudentIdFromFs(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .contentLength(pair.getFirst().length)
                .body(pair.getFirst());
    }
}
