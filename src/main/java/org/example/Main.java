package org.example;

import org.example.jpql.Member;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // Member 엔티티의 데이터 전체를 가져오기 때문에 Member로 타입 변환
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            // 결과가 1개 이상일때 getResultList()로 리스트로 반환  값이 없으면 빈 리스트 반환
            List<Member> resultList = query1.getResultList();

            for (Member member1 : resultList) {
                System.out.println("member = " + member1);
            }

            // 결과가 1개일 때 getSingleResult()로 단일 객체 반환
            // 결과가 없으면 없다는 오류 출력
            // 2개이상이면 noUnique 오류 출력
            Member result = query1.getSingleResult();
            System.out.println("result = " + result);

            // String 1개만 넘어오기 떄문에 String 으로 타입 변환
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // String과 int형 2개를 가져오기 떄문에 타입변환을 설정할 수 없다.
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            // 파라미터 바인딩 - 이름기준
            Member result1 = em.createQuery("select m from Member m where m.username =: username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("result1 = " + result1.getUsername());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}