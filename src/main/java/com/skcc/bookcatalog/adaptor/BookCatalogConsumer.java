package com.skcc.bookcatalog.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.bookcatalog.config.KafkaProperties;
import com.skcc.bookcatalog.domain.BookChanged;
import com.skcc.bookcatalog.repository.BookCatalogRepository;
import com.skcc.bookcatalog.service.BookCatalogService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BookCatalogConsumer {

    private final Logger log = LoggerFactory.getLogger(BookCatalogConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String TOPIC ="topic_catalog";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private BookCatalogRepository bookCatalogRepository;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BookCatalogService bookCatalogService;



    public BookCatalogConsumer(KafkaProperties kafkaProperties, BookCatalogRepository bookCatalogRepository, BookCatalogService bookCatalogService) {
        this.kafkaProperties = kafkaProperties;
        this.bookCatalogRepository = bookCatalogRepository;
        this.bookCatalogService = bookCatalogService;
    }

    @PostConstruct
    public void start(){
        log.info("Kafka consumer starting ...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer.subscribe(Collections.singleton(TOPIC));
        log.info("Kafka consumer started");

        executorService.execute(()-> {
                try {

                    while (!closed.get()){
                        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                        for(ConsumerRecord<String, String> record: records){
                            log.info("Consumed message in {} : {}", TOPIC, record.value());
                            ObjectMapper objectMapper = new ObjectMapper();
                            BookChanged bookChanged = objectMapper.readValue(record.value(), BookChanged.class);
                            bookCatalogService.processCatalogChanged(bookChanged);
                        }

                    }
                    kafkaConsumer.commitSync();

                }catch (WakeupException e){
                    if(!closed.get()){
                        throw e;
                    }

                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }finally {
                    log.info("kafka consumer close");
                    kafkaConsumer.close();
                }

            }



        );
    }




    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }

}
