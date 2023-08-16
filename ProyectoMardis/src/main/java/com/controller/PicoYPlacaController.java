package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller

public class PicoYPlacaController {

	@RequestMapping("/")
	//@GetMapping("/")
    public String mostrarFormulario(Model model) {
        model.addAttribute("consulta", new Consulta());
        return "formulario";
    }

    @PostMapping("/verificar")
    public String verificar(@ModelAttribute Consulta consulta, Model model) {
        boolean puedeCircular = verificarPicoYPlaca(consulta.getPlaca(), consulta.getHora(), consulta.getFecha());
        model.addAttribute("puedeCircular", puedeCircular);
        return "resultado";
    }

    private boolean verificarPicoYPlaca(String placa, String horaStr, String fechaStr) {
        // Obtener el último dígito de la placa
        int ultimoDigitoPlaca = Integer.parseInt(placa.substring(placa.length() - 1));
        
        // Convertir la hora y fecha de entrada a objetos de Java Time
        LocalTime hora = LocalTime.parse(horaStr);
        LocalDate fecha = LocalDate.parse(fechaStr);
        
        // Verificar si es sábado o domingo (libre circulación)
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            return true;
        }
        
        // Verificar si la hora está dentro de los horarios restringidos
        LocalTime inicioManana = LocalTime.parse("06:00");
        LocalTime finManana = LocalTime.parse("09:30");
        LocalTime inicioTarde = LocalTime.parse("16:00");
        LocalTime finTarde = LocalTime.parse("20:00");
        
        boolean horaRestringida = (hora.isAfter(inicioManana) && hora.isBefore(finManana)) ||
                                  (hora.isAfter(inicioTarde) && hora.isBefore(finTarde));
        
        // Verificar el último dígito de la placa según el día de la semana
        switch (diaSemana) {
            case MONDAY:
                return !(ultimoDigitoPlaca == 1 || ultimoDigitoPlaca == 2);
            case TUESDAY:
                return !(ultimoDigitoPlaca == 3 || ultimoDigitoPlaca == 4);
            case WEDNESDAY:
                return !(ultimoDigitoPlaca == 5 || ultimoDigitoPlaca == 6);
            case THURSDAY:
                return !(ultimoDigitoPlaca == 7 || ultimoDigitoPlaca == 8);
            case FRIDAY:
                return !(ultimoDigitoPlaca == 9 || ultimoDigitoPlaca == 0);
            default:
                return true; // En teoría, ya se verificó que es sábado o domingo
        }
}
}
