package com.wentworth.gagtakehome.services.impl;

import com.wentworth.gagtakehome.exceptions.FileParsingException;
import com.wentworth.gagtakehome.exceptions.FileValidationException;
import com.wentworth.gagtakehome.services.FileService;
import com.wentworth.gagtakehome.services.dto.TransportDTO;
import com.wentworth.gagtakehome.services.impl.validators.NumericValidator;
import com.wentworth.gagtakehome.services.impl.validators.StringValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private static final String PARSING_EXCEPTION = "Parsing failed because of ";
    private static final String PARSING_INVALID_FILE = "Parsing failed because of invalid file";
    private static final String VALIDATION_EMPTY_FILE = "Validation failed because empty file";
    private static final String VALIDATION_INVALID_FILE = "Validation failed because of invalid file";
    private static final String VALIDATION_INVALID_FIELDS = "Validation failed because of invalid fields";

    @Override
    public List<TransportDTO> parseFile(MultipartFile file, Boolean skipValidation) {
        if (skipValidation == null || !skipValidation) {
            boolean fileIsValid = isValid(file);

            if (!fileIsValid) {
                throw new FileParsingException(PARSING_INVALID_FILE);
            }
        }

        return parseStream(file);
    }

    private List<TransportDTO> parseStream(MultipartFile file) {
        List<TransportDTO> result = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                result.add(TransportDTO.build(line));
            }

            return result;
        } catch (FileParsingException e) {
            throw new FileParsingException(PARSING_INVALID_FILE);
        } catch (Exception e) {
            throw new FileParsingException(PARSING_EXCEPTION + e.getMessage());
        }
    }

    private boolean isValid(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileValidationException(VALIDATION_EMPTY_FILE);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader
                    .lines()
                    .allMatch(this::isValidLine);
        } catch (IOException e) {
            throw new FileValidationException(VALIDATION_INVALID_FILE);
        }
    }

    private boolean isValidLine(String line) {
        if (line == null || line.isEmpty()) {
            throw new FileValidationException(VALIDATION_INVALID_FIELDS);
        }

        return isValidFields(line);
    }

    private boolean isValidFields(String line) {
        String[] fields = line.split("\\|");
        if (fields.length != 7) {
            throw new FileValidationException(VALIDATION_INVALID_FIELDS);
        }

        if (StringValidator.isValidUUID(fields[0])
                && StringValidator.isValidAlphaNumeric(fields[1])
                && StringValidator.isValidAlpha(fields[2])
                && StringValidator.isValidAlpha(fields[3])
                && StringValidator.isValidAlpha(fields[4])
                && NumericValidator.isValidDouble(fields[5])
                && NumericValidator.isValidDouble(fields[6])) {
            return true;
        } else {
            throw new FileValidationException(VALIDATION_INVALID_FIELDS);
        }
    }
}
