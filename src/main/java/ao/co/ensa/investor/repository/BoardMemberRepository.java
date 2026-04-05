package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

    List<BoardMember> findAllByActiveTrueOrderByDisplayOrderAsc();
}
