package abc.cakesweet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import abc.cakesweet.CosData;

@Repository
public interface CosDataRepository extends JpaRepository<CosData, Long> {
    public Optional<CosData> findByCosid(Long cosid);

    public List<CosData> findByMyid(Long myid);

    public void deleteByCosid(Long cosid);
}