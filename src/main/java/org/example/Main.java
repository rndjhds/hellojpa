package org.example;

import org.example.jpql.Member;
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

            String jpql = "select distinct t from Team t join fetch t.members";
            List<Team> result = em.createQuery(jpql, Team.class)
                    .getResultList();
            for (Team team : result) {
                //페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩X
                System.out.println("team = " + team.getName() + ", " + team.getMembers().size());
                for(Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
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