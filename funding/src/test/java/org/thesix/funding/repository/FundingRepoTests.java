package org.thesix.funding.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class FundingRepoTests {

    @Autowired
    private FundingRepository fundingRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     *  펀딩 글 등록(insert) 테스트
     */
    @Test
    public void testInsertFunding(){
        IntStream.rangeClosed(1,100).forEach(i->{

            LocalDateTime ldt = LocalDateTime.now();
            ldt.plusYears(1);

            Funding funding = Funding.builder()
                    .title("제목.."+i)
                    .writer("작성자" + i)
                    .email("user"+i+"@aaa.com")
                    .content("내용...."+i)
                    .dueDate(ldt)
                    .success(false)
                    .removed(false)
                    .build();

            fundingRepository.save(funding);
        });
    }

    /**
     * 펀딩 제품 등록(insert) 테스트
     */
    @Test
    public void testInsertProduct(){

        IntStream.rangeClosed(1,200).forEach(i->{

            long fno = (int)(Math.random()*100) + 1;

            Funding funding = Funding.builder()
                    .fno(fno).build();

            Product product = Product.builder()
                    .name("제품.."+i)
                    .des(i+"번 굿즈입니다.")
                    .price(1000)
                    .funding(funding).build();

            productRepository.save(product);
        });
    }

    /**
     *  요청한 펀딩 fno와 일치하는 제품 리스트를 읽어오는 테스트
     */
    @Test
    public void testList1(){

        Optional<Product[]> result = productRepository.findByFundingId(5L);

        result.ifPresent(product -> System.out.println(Arrays.stream(product).collect(Collectors.toList())));
    }

    
    @Test
    public void getList2(){



    }
}
