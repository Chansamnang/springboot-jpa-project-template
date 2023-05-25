package com.samnang.project.template.db.repository;


import com.samnang.project.template.db.entity.BindIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BindIpRepository extends JpaRepository<BindIpAddress, Long> {

}
