package org.example;

import org.example.jpql.Address;
import org.example.jpql.Member;
import org.example.jpql.MemberDTO;
import org.example.jpql.Team;

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

            em.flush();
            em.clear();


            List<MemberDTO> result = em.createQuery("select new org.example.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("MemberDTO = " + memberDTO.getUsername());
            System.out.println("MemberDTO = " + memberDTO.getAge());

          /*  Member findMember = result.get(0);
            findMember.setAge(20);
*/
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