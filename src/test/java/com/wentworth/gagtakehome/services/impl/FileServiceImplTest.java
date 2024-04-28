package com.wentworth.gagtakehome.services.impl;

import com.wentworth.gagtakehome.AbstractTest;
import com.wentworth.gagtakehome.exceptions.FileParsingException;
import com.wentworth.gagtakehome.exceptions.FileValidationException;
import com.wentworth.gagtakehome.services.dto.TransportDTO;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceImplTest extends AbstractTest {

    FileServiceImpl fileService = new FileServiceImpl();

    @Test
    void testParseGoodFileNullSkipValidationSuccess() throws IOException {
        List<TransportDTO> actual = fileService.parseFile(getMultipartFile("good.txt"), null);
        assertGoodFile(actual);
    }

    @Test
    void testParseGoodFileFalseSkipValidationSuccess() throws IOException {
        List<TransportDTO> actual = fileService.parseFile(getMultipartFile("good.txt"), false);
        assertGoodFile(actual);
    }

    @Test
    void testParseGoodFileTrueSkipValidationSuccess() throws IOException {
        List<TransportDTO> actual = fileService.parseFile(getMultipartFile("good.txt"), true);
        assertGoodFile(actual);
    }

    @Test
    void testParseBadFileNullSkipValidationFail() {
        FileValidationException exception = assertThrows(
                FileValidationException.class,
                () -> fileService.parseFile(getMultipartFile("bad.txt"), null));
        assertEquals("Validation failed because of invalid fields", exception.getMessage());
    }

    @Test
    void testParseBadFileFalseSkipValidationFail() {
        FileValidationException exception = assertThrows(
                FileValidationException.class,
                () -> fileService.parseFile(getMultipartFile("bad.txt"), false));
        assertEquals("Validation failed because of invalid fields", exception.getMessage());
    }

    @Test
    void testParseBadFileTrueSkipValidationFail() {
        FileParsingException exception = assertThrows(
                FileParsingException.class,
                () -> fileService.parseFile(getMultipartFile("bad.txt"), true));
        assertEquals("Parsing failed because of Invalid UUID string: this is a bad file!!!!!", exception.getMessage());
    }

    private static @NotNull MultipartFile getMultipartFile(String fileName) throws IOException {
        return new MockMultipartFile(
                "file", fileName, "text/plain",
                new ClassPathResource(fileName).getInputStream());
    }

    private static void assertGoodFile(List<TransportDTO> actual) {
        assertNotNull(actual);
        assertEquals(3, actual.size());

        assertEquals(UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"), actual.get(0).uuid());
        assertEquals("1X1D14", actual.get(0).id());
        assertEquals("John Smith", actual.get(0).name());
        assertEquals("Likes Apricots", actual.get(0).likes());
        assertEquals("Rides A Bike", actual.get(0).transport());
        assertEquals(6.2, actual.get(0).avgSpeed());
        assertEquals(12.1, actual.get(0).topSpeed());

        assertEquals(UUID.fromString("3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7"), actual.get(1).uuid());
        assertEquals("2X2D24", actual.get(1).id());
        assertEquals("Mike Smith", actual.get(1).name());
        assertEquals("Likes Grape", actual.get(1).likes());
        assertEquals("Drives an SUV", actual.get(1).transport());
        assertEquals(35.0, actual.get(1).avgSpeed());
        assertEquals(95.5, actual.get(1).topSpeed());

        assertEquals(UUID.fromString("1afb6f5d-a7c2-4311-a92d-974f3180ff5e"), actual.get(2).uuid());
        assertEquals("3X3D35", actual.get(2).id());
        assertEquals("Jenny Walters", actual.get(2).name());
        assertEquals("Likes Avocados", actual.get(2).likes());
        assertEquals("Rides A Scooter", actual.get(2).transport());
        assertEquals(8.5, actual.get(2).avgSpeed());
        assertEquals(15.3, actual.get(2).topSpeed());
    }
}
