package org.example;

import org.example.jpql.Member;
import org.example.jpql.MemberDTO;
import org.example.jpql.MemberType;
import org.example.jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.changeTeam(teamA);
            member1.setType(MemberType.ADMIN);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.changeTeam(teamA);
            member2.setType(MemberType.ADMIN);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원2");
            member3.setAge(10);
            member3.changeTeam(teamB);
            member3.setType(MemberType.ADMIN);
            em.persist(member3);

            em.flush();
            em.clear();

            //String jpql = "select m from Member m where m =: member";
            //String jpql = "select distinct t from Team t join fetch t.members m join fetch m.team"; // 예시로 별칭을 줄 수 있는 경우
            //String jpql = "select distinct t from Team t join fetch t.members"; // 컬렉션 연관 필드와 페치 조인 값이 뻥튀기 되기 때문에 distinct로 중복된 데이터 제거
            //String jpql = "select m from Member m join fetch m.team"; // 단일 연관 필드와 페치 조인
            /*Member findMember = em.createQuery(jpql, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            */

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();
            for (Member member : resultList){
                System.out.println("member = " + member);
            }

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