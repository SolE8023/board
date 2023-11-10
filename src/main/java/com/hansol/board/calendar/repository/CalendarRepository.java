package com.hansol.board.calendar.repository;

import com.hansol.board.calendar.entity.Calendar;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("select c from Calendar c where " +
            "(c.startDate <= :endRange and c.endDate >= :startRange) and " +
            "c.endDate != null and c.startDate != c.endDate")
    List<Calendar> getLongTermEvents(@Param("startRange") LocalDate startRange, @Param("endRange") LocalDate endRange);

    @Query("select c from Calendar c where " +
            "(c.startDate <= :endRange and c.startDate >= :startRange) and " +
            "c.endDate = null or c.startDate = c.endDate")
    List<Calendar> getSingleDayEvents(@Param("startRange") LocalDate startRange, @Param("endRange") LocalDate endRange);
}
