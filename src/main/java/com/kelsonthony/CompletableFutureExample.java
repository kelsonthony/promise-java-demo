package com.kelsonthony;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    public static void main(String[] args) {
        // Criando uma CompletableFuture para chamar o endpoint da API
        CompletableFuture<HttpResponse<String>> promessa = CompletableFuture.supplyAsync(() -> {
            HttpResponse<String> response;
            try {
                // Criando uma requisição GET para o endpoint da API
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8081/v1/clients"))
                        .build();
                // Enviando a requisição e recebendo a resposta
                HttpClient client = HttpClient.newHttpClient();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Verificando se a resposta foi bem-sucedida (código 2xx)
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    System.out.println("Resposta da API recebida com sucesso");
                    System.out.println("body response" + response.body());
                } else {
                    throw new RuntimeException("Erro ao chamar a API. Código de status: " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Erro ao chamar a API: " + e.getMessage());
            }
            return response;
        });
        // Usando a CompletableFuture para lidar com o resultado ou exceção
        promessa.thenAccept((resultado) -> {
            System.out.println("Operação concluída com sucesso");
            System.out.println("resultado: " + resultado);
        }).exceptionally((ex) -> {
            System.out.println("Erro ao realizar a operação: " + ex.getMessage());
            return null;
        });

        // Aguarda a conclusão da CompletableFuture
        try {
            Thread.sleep(2000); // Espera 2 segundos para a operação assíncrona ser concluída
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
