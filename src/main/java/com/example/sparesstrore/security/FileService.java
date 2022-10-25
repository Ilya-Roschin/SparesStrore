package com.example.sparesstrore.security;

import com.example.sparesstrore.entity.Car;
import com.example.sparesstrore.entity.Spare;
import com.example.sparesstrore.repository.CarRepository;
import com.example.sparesstrore.repository.SpareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileService {

    @Autowired
    private SpareRepository spareRepository;

    @Autowired
    private CarRepository carRepository;

    public void addImageToSpare(final long id, MultipartFile multipartFile) {
        String fileName = "image/spare" + id + ".png";
        try {
            File file = new File("src/main/resources/static/" + fileName);
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("file exception");
        }
        Spare spare = spareRepository.findById(id).orElseThrow();
        spare.setSpareUrl(fileName);
        spareRepository.save(spare);
    }

    public void addImageToCar(final long id, MultipartFile multipartFile) {
        String fileName = "image/car" + id + ".png";
        try {
            File file = new File("src/main/resources/static/" + fileName);
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("file exception");
        }
        Car car = carRepository.findById(id).orElseThrow();
        car.setCarUrl(fileName);
        carRepository.save(car);
    }
}
