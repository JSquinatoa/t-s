dependencias
implementation("org.springframework.boot:spring-boot-starter-webmvc")

implementation("org.springframework.ai:spring-ai-starter-vector-store-qdrant")

implementation("org.springframework.ai:spring-ai-tika-document-reader")

implementation("org.springframework.ai:spring-ai-starter-model-transformers")

implementation("org.springframework.ai:spring-ai-starter-model-openai")

implementation("org.apache.camel.springboot:camel-spring-boot-starter:4.20.0")

implementation("org.apache.camel.springboot:camel-file-starter:4.20.0")

implementation("org.springframework.ai:spring-ai-vector-store-advisor")

yml

server:
    port: 8090
app:
    key: ${APP_KEY}
    aimodel: llama-3.2-1b-instruct-q4_k_m
    url: http://127.0.0.1:8080
spring:
    ai:
        model:
            embedding: transformers
        embedding:
            transformer:
                onnx:
                    model-uri: classpath:models/model.onnx
                tokenizer:
                    uri: classpath:models/tokenizer.json    
    openai:
    api-key: ${app.key}
    base-url: ${app.url}
    chat:
        model: ${app.aimodel}
    vectorstore:
        qdrant:
            collection-name: springai
            initialize-schema: true

declar prompt
@Value("classpath:/")
Resource

para el lector de archivo extiendo de RouteBuilder

para extraer documento reibe un file y se proceso con Resource

para cortar los documentos se usa TokenTextSplitter

el VectoreStore siempre es autowired

//1. poner las variables

// 2. declarar el constructor

//3. metodo post par enviar peticion
//4. coger los datos del usuarrio

//5. hacemo el q advisor
var qaAdvisro = QuestionAnswerAdvisor.builder(vectorStore)
       DOBLE SEARHC 

//6. Llamamos al modelo de chatClient pasando ela dvisor
Flux<ServerSentEvent<String>> token

//7. la ia responde en flujo y avismao e el front qe deje de pensar
Flux<ServerSentEvent<String>> completado = Flux.just(

// 8 Uniomos todo y deovemos en lufjo

return token.concatWith(completado)
.onErrorResume(error -> Flux.just(
ServerSentEvent.<String>builder()
.event("error")
.data(error.getMessage())
.build()
));
