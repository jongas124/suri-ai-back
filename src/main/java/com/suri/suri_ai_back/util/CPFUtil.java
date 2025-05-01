package com.suri.suri_ai_back.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.suri.suri_ai_back.exceptions.InvalidCPFException;

@Component
public class CPFUtil {

    //valida CPFs no formato 000.000.000-00 ou 00000000000
    private static final String CPF_REGEX = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$";

    //verificar se o CPF é válido
    public static void validarCPF(String cpf) {
        if (!verificaFormato(cpf)) {
            throw new InvalidCPFException("Formato de CPF inválido.");
        }

        //remover pontos e hífen
        String numerosCPF = cpf.replaceAll("[^\\d]", "");

        if (numerosCPF.length() != 11) {
            throw new InvalidCPFException("Formato de CPF inválido.");
        }

        //calcular os dígitos verificadores
        try {
            int[] digitos = new int[11];
            for (int i = 0; i < 11; i++) {
                digitos[i] = Integer.parseInt(numerosCPF.substring(i, i + 1));
            }

            int digitoVerificador1 = calculateDigit(digitos, 9);
            int digitoVerificador2 = calculateDigit(digitos, 10);

            //verificar se os dígitos verificadores calculados são iguais aos informados
            //se forem a função deixa de lançar a exceção e o CPF é considerado válido
            if(digitoVerificador1 != digitos[9] || digitoVerificador2 != digitos[10]) {
                throw new InvalidCPFException("CPF inválido.");
            }
            
        } catch (NumberFormatException e) {
            throw new InvalidCPFException("Formato de CPF inválido.");
        }
    }

    //verificar o formato do CPF usando regex
    private static boolean verificaFormato(String cpf) {
        Pattern pattern = Pattern.compile(CPF_REGEX);
        Matcher matcher = pattern.matcher(cpf);
        return matcher.matches();
    }

    //calcular o dígito verificador
    private static int calculateDigit(int[] digitos, int tamanho) {
        int soma = 0;
        //verifica se os pesos devem começar em 1 (para o tamanho 9) ou 0 (para o tamanho 10)
        int inicioPesos = 10 - tamanho;
        for (int i = 0; i < tamanho; i++) {
            soma += digitos[i] * (inicioPesos + i);
        }
        int digito = soma % 11;
        if(digito >= 10) {
            return 0;
        } else {
            return digito;
        }
    }

    //formatar CPF
    public static String formatarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}