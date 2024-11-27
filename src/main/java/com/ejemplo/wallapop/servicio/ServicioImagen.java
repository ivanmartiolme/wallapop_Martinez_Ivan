package com.ejemplo.wallapop.servicio;

import com.ejemplo.wallapop.modelo.Imagen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ServicioImagen {

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;

    public Imagen guardarImagen(MultipartFile archivo) throws IOException {
        // Crear una nueva entidad Imagen
        Imagen imagen = new Imagen();
        imagen.setNombre(archivo.getOriginalFilename());
        imagen.setDatos(archivo.getBytes()); // Guardar los bytes del archivo (opcional, depende de tu implementación)
        imagen.setTipo(archivo.getContentType());

        // Aquí podrías guardar la imagen en un almacenamiento externo o en base de datos
        // imagenRepositorio.save(imagen);

        return imagen;
    }

    public void eliminarImagen(String nombreArchivo) throws IOException {
        Path rutaCompleta = Paths.get(uploadDir, nombreArchivo);
        Files.deleteIfExists(rutaCompleta);
    }


    private String obtenerExtension(String nombreArchivo) {
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);
    }
}