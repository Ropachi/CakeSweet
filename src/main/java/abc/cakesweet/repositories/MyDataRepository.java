package abc.cakesweet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import abc.cakesweet.MyData;

@Repository
public interface MyDataRepository extends JpaRepository<MyData, Long> {
    public List<MyData> findAll();

    public Optional<MyData> findById(long id);
}