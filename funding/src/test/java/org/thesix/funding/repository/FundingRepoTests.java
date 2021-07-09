package org.thesix.funding.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thesix.funding.entity.Favorite;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;
import java.time.LocalDateTime;
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
    @Autowired
    private FavoriteRepository favoriteRepository;

    /**
     * 펀딩 글 등록(insert) 테스트
     */
    @Test
    public void testInsertFunding() {
        IntStream.rangeClosed(1, 100).forEach(i -> {

            LocalDateTime ldt = LocalDateTime.now();
            ldt.plusYears(1);

            Funding funding = Funding.builder()
                    .title("제목.." + i)
                    .writer("작성자" + i)
                    .email("user" + i + "@aaa.com")
                    .content("내용...." + i)
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
    public void testInsertProduct() {

        IntStream.rangeClosed(1, 200).forEach(i -> {

            long fno = (int) (Math.random() * 100) + 1;

            Funding funding = Funding.builder()
                    .fno(fno).build();

            Product product = Product.builder()
                    .name("제품.." + i)
                    .des(i + "번 굿즈입니다.")
                    .price(1000)
                    .funding(funding).build();

            productRepository.save(product);
        });
    }

    /**
     * 펀딩 찜 등록(insert) 테스트
     */
    @Test
    public void testInsertFavorite(){

        IntStream.rangeClosed(1, 200).forEach(i -> {

            long fno = (int) (Math.random() * 100) + 1;

            Funding funding = Funding.builder()
                    .fno(fno).build();

            Favorite favorite = Favorite.builder()
                    .mark(true)
                    .actor("사용자.."+i)
                    .funding(funding).build();

            favoriteRepository.save(favorite);

        });
    }

    /**
     * 요청한 펀딩 fno와 일치하는 제품 리스트를 읽어오는 테스트
     */
    @Test
    public void testList1() {

        Optional<Product[]> result = productRepository.getProductById(5L);

        System.out.println(Arrays.stream(result.get()).collect(Collectors.toList()));
    }


    /**
     * 펀딩 글 목록만 가져오는 테스트
     * Object[] : Funding, count(Favorite), count(Product)
     */
    @Test
    public void getList2() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Object[]> result = fundingRepository.getData(pageable);

        result.getContent().forEach(arr -> System.out.println(Arrays.toString(arr)));

    }

    /**
     * 글목록 + 검색 + 페이징 기능 테스트
     * Object[] : Funding객체, count(Product), count(Favorite)  + 제품 대표이미지 어케 처리???
     */
    @Test
    public void testGetSearchList(){

        Pageable pageable = PageRequest.of(0,10);

        String keyword = "10";
        String type = "tcw";

        Page<Object[]> list = fundingRepository.getListSearch(keyword, type, pageable);

        list.getContent().forEach(list1-> System.out.println(Arrays.toString(list1)));

    }

    /**
     * 펀딩 글 하나만 가져오는 테스트
     * 필요한 데이터 : 제품 이미지 리스트, 제품정보, 글 정보
     */
    @Test
    public void getList3(){

        Optional<Funding> result1 = fundingRepository.getFundingById(2L);
        Object result2 = productRepository.getProductById(2L);
    }

    /**
     * 펀딩 글 내용을 수정하는 테스트
     */
    @Test
    public void testModify(){

        Optional<Funding> result = fundingRepository.findById(2L);
        result.ifPresent(funding -> {

            LocalDateTime ldt = LocalDateTime.now();
            ldt.plusYears(3);

            funding.changeTitle("change Title");
            funding.changeContent("change Content");
            funding.changeDueDate(ldt);

            fundingRepository.save(funding);
        });

    }

    /**
     * 펀딩 글을 삭제 상태로 바꾸는 테스트
     */
    @Test
    public void testRemove(){

        Optional<Funding> result = fundingRepository.findById(2L);

        result.ifPresent(result1->{
            System.out.println(result1);
            result1.changeRemoved(true);
            fundingRepository.save(result1);
        });
    }


}
