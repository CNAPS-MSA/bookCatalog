package com.skcc.bookcatalog.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.skcc.bookcatalog.config.KafkaProperties;
import com.skcc.bookcatalog.domain.BookCatalog;
import com.skcc.bookcatalog.domain.BookCatalogEvent;
import com.skcc.bookcatalog.repository.BookCatalogRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.print.Book;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BookCatalogKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(BookCatalogKafkaConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String TOPIC ="topic_catalog";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private BookCatalogRepository bookCatalogRepository;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");




    public BookCatalogKafkaConsumer(KafkaProperties kafkaProperties,  BookCatalogRepository bookCatalogRepository) {
        this.kafkaProperties = kafkaProperties;
        this.bookCatalogRepository = bookCatalogRepository;
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
                            BookCatalogEvent bookCatalogEvent = objectMapper.readValue(record.value(), BookCatalogEvent.class);
                            String eventType  = bookCatalogEvent.getEventType();

                            if(eventType.equals("NEW_BOOK")){
                                BookCatalog bookCatalog = new BookCatalog();
                                bookCatalog.setAuthor(bookCatalogEvent.getAuthor());
                                bookCatalog.setClassification(bookCatalogEvent.getClassification());
                                bookCatalog.setDescription(bookCatalogEvent.getDescription());
                                bookCatalog.setPublicationDate(LocalDate.parse(bookCatalogEvent.getPublicationDate(), fmt));
                                bookCatalog.setRented(bookCatalogEvent.getRented());
                                bookCatalog.setTitle(bookCatalogEvent.getTitle());
                                bookCatalog.setRentCnt(bookCatalogEvent.getRentCnt());
                                bookCatalogRepository.save(bookCatalog);
                            }else if(eventType.equals("DELETE_BOOK")){
                                bookCatalogRepository.deleteByTitle(bookCatalogEvent.getTitle());
                            }else if(eventType.equals("RENT_BOOK")){
                                BookCatalog bookCatalog = bookCatalogRepository.findByTitle(bookCatalogEvent.getTitle());
                                Long newCnt = bookCatalog.getRentCnt()+(long)1;
                                bookCatalog.setRentCnt(newCnt);
                                bookCatalog.setRented(true);
                                bookCatalogRepository.save(bookCatalog);
                            }else if(eventType.equals("RETURN_BOOK")){
                                BookCatalog bookCatalog = bookCatalogRepository.findByTitle(bookCatalogEvent.getTitle());
                                bookCatalog.setRented(false);
                                bookCatalogRepository.save(bookCatalog);
                            }else if(eventType.equals("UPDATE_BOOK")){
                                BookCatalog bookCatalog = bookCatalogRepository.findByTitle(bookCatalogEvent.getTitle());
                                bookCatalog.setAuthor(bookCatalogEvent.getAuthor());
                                bookCatalog.setClassification(bookCatalogEvent.getClassification());
                                bookCatalog.setDescription(bookCatalogEvent.getDescription());
                                bookCatalog.setPublicationDate(LocalDate.parse(bookCatalogEvent.getPublicationDate(), fmt));
                                bookCatalog.setRented(bookCatalogEvent.getRented());
                                bookCatalog.setTitle(bookCatalogEvent.getTitle());
                                bookCatalog.setRentCnt(bookCatalogEvent.getRentCnt());
                                bookCatalogRepository.save(bookCatalog);
                            }


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
