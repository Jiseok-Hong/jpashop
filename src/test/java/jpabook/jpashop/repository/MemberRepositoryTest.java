package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest //integrate test code with spring boot
@Transactional // roll back data of test code
public class MemberRepositoryTest {


    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void createAccount() throws Exception {
        //given
        Member member = new Member();
        member.setName("John");
        //when
        Long findId = memberService.join(member);
        //then
        assertEquals(member, memberRepository.findOne(findId));
    }


    @Test(expected = IllegalStateException.class)
    public void checkDuplicatedAccount() throws Exception {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName("john");
        member2.setName("john");
        //when
        Long findId1 = memberService.join(member1);
        Long findId2 = memberService.join(member2);

        //then
        fail("Error should be occured");
    }
}