package com.kelsonthony;

import com.google.common.util.concurrent.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GuavaListenableFutureExample {
    public static void main(String[] args) {
        // Criando um ListeningExecutorService usando o ThreadPool padrão do Guava
        ListeningExecutorService service = MoreExecutors.listeningDecorator(MoreExecutors.newDirectExecutorService());

        // Criando um ListenableFuture para chamar o endpoint da API
        ListenableFuture<HttpResponse<String>> future = service.submit(() -> {
            try {
                // Criando uma requisição GET para o endpoint da API
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8081/v1/clients"))
                        .build();

                // Enviando a requisição e recebendo a resposta
                HttpClient client = HttpClient.newHttpClient();
                return client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao chamar a API: " + e.getMessage(), e);
            }
        });

        // Adicionando um listener para lidar com o resultado ou exceção
        Futures.addCallback(future, new FutureCallback<>() {
            @Override
            public void onSuccess(HttpResponse<String> response) {
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    System.out.println("Resposta da API recebida com sucesso:");
                    System.out.println("response body" + response.body());
                } else {
                    System.err.println("Erro ao chamar a API. Código de status: " + response.statusCode());
                }
                service.shutdown(); // Encerrando o serviço após o uso
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.err.println("Erro ao realizar operação: " + throwable.getMessage());
                service.shutdown(); // Encerrando o serviço após o uso
            }
        }, service);
    }
}
