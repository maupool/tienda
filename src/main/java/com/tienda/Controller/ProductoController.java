/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final ProductoService productoService;
    private final MessageSource messageSource;

    public ProductoController(ProductoService productoService,
                              ProductoService categoriyService,
                              MessageSource messageSource) {
        this.productoService = productoService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        
        return "/producto/listado";
    }
    @PostMapping("/guardar")
public String guardar(
        @Valid Producto producto,
        @RequestParam MultipartFile imagenFile,
        RedirectAttributes redirectAttributes) {

    productoService.save(producto, imagenFile);

    redirectAttributes.addFlashAttribute(
            "codOk",
            messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
    );

    return "redirect:/producto/listado";
}

@PostMapping("/eliminar")
public String eliminar(
        @RequestParam Integer idProducto,
        RedirectAttributes redirectAttributes) {

    String titulo = "codOk";
    String detalle = "mensaje.eliminado";

    try {
        productoService.delete(idProducto);
    } catch (IllegalArgumentException e) {
        // excepción de argumento inválido (ej: no existe)
        titulo = "error";
        detalle = "error1";
    } catch (IllegalStateException e) {
        // excepción de estado ilegal (ej: datos asociados)
        titulo = "error";
        detalle = "categoria.error2";
    } catch (Exception e) {
        // cualquier otra excepción inesperada
        titulo = "error";
        detalle = "categoria.error3";
    }

    redirectAttributes.addFlashAttribute(
            titulo,
            messageSource.getMessage(detalle, null, Locale.getDefault())
    );

    return "redirect:/producto/listado";
}

@GetMapping("/modificar/{idProducto}")
public String modificar(
        @PathVariable("idProducto") Integer idProducto,
        Model model,
        RedirectAttributes redirectAttributes) {

    Optional<Producto> productoOpt = productoService.getProducto(idProducto);

    if (productoOpt.isEmpty()) {
        redirectAttributes.addFlashAttribute(
                "error",
                messageSource.getMessage("producto.error01", null, Locale.getDefault())
        );
        return "redirect:/producto/listado";
    }

    // ↓↓↓ Parte resaltada en la imagen ↓↓↓
    var producto = productoService.getProductos(true);
    model.addAttribute("categorias", producto);

    model.addAttribute("producto", productoOpt.get());
    return "producto/modificar";
}
}
