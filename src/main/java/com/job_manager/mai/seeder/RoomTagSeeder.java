package com.job_manager.mai.seeder;

import com.job_manager.mai.contrains.RoomTags;
import com.job_manager.mai.model.RoomTag;
import com.job_manager.mai.repository.RoomTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoomTagSeeder implements CommandLineRunner, Ordered {
    private final RoomTagRepository roomTagRepository;

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(RoomTags.values()).forEach((tagName) -> {
            RoomTag roomTag = roomTagRepository.findByName(tagName.getVal()).orElse(null);
            if (roomTag == null) {
                roomTag = new RoomTag(tagName.getVal());
                roomTagRepository.saveAndFlush(roomTag);
            }
        });
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
