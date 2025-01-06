package org.genai.fusion.semantickernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.data.VolatileVectorStore;
import com.microsoft.semantickernel.data.VolatileVectorStoreRecordCollectionOptions;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.genai.fusion.model.LoanApplication;
import org.genai.fusion.semantickernel.search.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.reflections.Reflections.log;

@Configuration
public class SKConfigs {
    @Value("${client.azureopenai.key}")
    String apiKey;
    @Value("${client.azureopenai.endpoint}")
    String endpoint;
    @Value("${client.azureopenai.deploymentname}")
    String deploymentName;
    @Value("${client.azureopenai.embedding.model}")
    String embeddingModel;
    @Value("${client.azureopenai.embedding.dimension}")
    String embeddingDimension;

    private OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildAsyncClient();

    }

    private ChatCompletionService chatCompletionService() {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(openAIAsyncClient())
                .withModelId(deploymentName)
                .build();
    }

    @Bean
    public Kernel kernel(SKPlugins skPlugins) {
        KernelPlugin kernelPlugin = KernelPluginFactory.createFromObject(
                skPlugins,
                "SKPlugins");

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService())
                .withPlugin(kernelPlugin)
                .build();
    }

    @Bean
    public OpenAITextEmbeddingGenerationService embeddingGenerationService() {
        return OpenAITextEmbeddingGenerationService.builder()
                .withOpenAIAsyncClient(openAIAsyncClient())
                .withDeploymentName(embeddingModel)
                .withModelId(embeddingModel)
                .withDimensions(Integer.parseInt(embeddingDimension))
                .build();
    }

    @Bean
    public VectorStoreRecordCollection<String, Document> inMemoryVectorStore() {
        VolatileVectorStore volatileVectorStore = new VolatileVectorStore();

        var collectionName = "knowledge-base";

        return volatileVectorStore.getCollection(collectionName,
                VolatileVectorStoreRecordCollectionOptions.<Document>builder()
                        .withRecordClass(Document.class)
                        .build());
    }

    @Bean
    public ContextVariableTypeConverter<String> stringContextVariableTypeConverter(ObjectMapper objectMapper) {
        return new ContextVariableTypeConverter<>(
                String.class,
                objectToObject -> (String) objectToObject,
                o -> {
                    try {
                        String json = objectMapper.writeValueAsString(o);
                        log.debug("converting from object to json {}", json);
                        return json;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                s -> {
                    try {
                        log.debug("converting from json to object {}", s);
                        return objectMapper.readValue(s, String.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Bean
    public ContextVariableTypeConverter<LoanApplication> loanApplicationContextVariableTypeConverter(
            ObjectMapper objectMapper) {
        return new ContextVariableTypeConverter<>(
                LoanApplication.class,
                objectToObject -> (LoanApplication) objectToObject,
                o -> {
                    try {
                        String json = objectMapper.writeValueAsString(o);
                        log.debug("converting from object to json {}", json);
                        return json;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                s -> {
                    try {
                        log.debug("converting from json to object {}", s);
                        return objectMapper.readValue(s, LoanApplication.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
