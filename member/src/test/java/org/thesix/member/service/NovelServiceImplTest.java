package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thesix.member.entity.Novels;
import org.thesix.member.repository.NovelRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class NovelServiceImplTest {

    @Autowired
    private NovelRepository novelRepository;

    @Test
    public void novelDelete(){

        Novels novels = novelRepository.findById(40L).orElseThrow(() -> new NullPointerException("널포인트~"));

        novels.changeDelete();

        novelRepository.save(novels);

    }


}