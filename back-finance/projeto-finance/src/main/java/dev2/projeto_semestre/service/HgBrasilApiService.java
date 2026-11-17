package dev2.projeto_semestre.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev2.projeto_semestre.exceptions.ExternalServiceException;

@Service
public class HgBrasilApiService {

    private final String API_KEY = "2aef7024"; 
    private final String BASE_URL = "https://api.hgbrasil.com/finance";

    public BigDecimal buscarPrecoAtivo(String ticker) {
    String url = UriComponentsBuilder
            .fromHttpUrl("https://api.hgbrasil.com/v2/finance/quotes")
            .queryParam("tickers", "B3:" + ticker.toUpperCase())
            .queryParam("key", apiKey)          
            .build().encode().toUriString();
    try {
        Map resposta = restTemplate.getForObject(url, Map.class);
        List<Map> results = (List<Map>) resposta.get("results");
        if (results == null || results.isEmpty())
            throw new ExternalServiceException("Ativo " + ticker + " não encontrado na B3.");
        Map quote = (Map) results.get(0).get("quote");
        return new BigDecimal(quote.get("value").toString());
    } catch (ExternalServiceException e) {
        throw e;                                 
    } catch (Exception e) {
        throw new ExternalServiceException("Erro ao consultar HG Brasil.", e);
    }
}