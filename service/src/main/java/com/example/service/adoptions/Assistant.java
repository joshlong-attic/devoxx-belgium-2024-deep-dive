package com.example.service.adoptions;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
class Assistant {

    @Bean
    ChatClient client(ChatClient.Builder builder, DogRepository dogRepository, VectorStore vectorStore) {
        if (false)
            dogRepository.findAll().forEach(dog -> {
                var dogument = new Document("id: %s, name: %s, description: %s".formatted(dog.id(), dog.name(), dog.description()));
                vectorStore.add(List.of(dogument));
            });

        return builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .defaultSystem("""
                        You are an AI powered assistant to help people adopt a dog 
                        from the adoption agency named Pooch Palace with locations in 
                        Seoul, Tokyo, Singapore, Paris, Antwerp, Mumbai, New Delhi, Stuttgart, 
                        San Francisco, and London. If you don't know about the dogs housed at 
                        our particular stores, then return a disappointed response suggesting 
                        we don't have any dogs available.
                        """)
                .build();
    }

}

@Controller
@ResponseBody
class AssistantController {

    private final ChatClient chatClient;

    AssistantController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/search")
    Collection<DogAdoptionSuggestion> search(@RequestBody Map<String, String> query) {
        return List.of(this.chatClient
                .prompt()
                .user(query.get("query"))
                .call()
                .entity(DogAdoptionSuggestion.class));
    }
}

record DogAdoptionSuggestion(int id, String name, String description) {
}