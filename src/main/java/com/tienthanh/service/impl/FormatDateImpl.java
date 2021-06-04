package com.tienthanh.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FormatDateImpl {
	public Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public List<Date> findDaysOfWeek(LocalDateTime localDate) {
		// tra ve cac ngay trong tuan
		List<Date> result = new ArrayList<>();
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		int day = dayOfWeek.getValue();
		for (int i = 1; i <= 7; i++) {
			if (i < day) {
				Date date = Date.from(localDate.minusDays(day - i).atZone(ZoneId.systemDefault()).toInstant());
				result.add(date);
			} else {
				Date date = Date.from(localDate.plusDays(i - day).atZone(ZoneId.systemDefault()).toInstant());
				result.add(date);
			}
		}

		return result;
	}
}
