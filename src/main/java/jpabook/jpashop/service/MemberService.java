package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //@AllArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;


//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //REGISTER ACCOUNT
    @Transactional
    public Long join(Member member) {
        //CHECK WHETHER THERE IS EXISTED ACCOUNT WITH SAME USERNAME
        ValidateDuplicatedMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void ValidateDuplicatedMember(Member member) {
        List<Member> findMember = memberRepository.findByName(member.getName());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("The user is already existed");
        }
    }

    //LOOK UP ALL THE MEMBERS
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name){
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
    }
}
