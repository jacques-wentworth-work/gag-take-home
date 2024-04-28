package com.wentworth.gagtakehome.jpa;

import com.wentworth.gagtakehome.jpa.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, String> {
}
