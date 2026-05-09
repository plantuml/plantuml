package net.sourceforge.plantuml.utils;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Locale;

// Generated — do not edit
// Build by I18nDataTimeGenerator
public class I18nTimeData {

	public static String shortName(DayOfWeek dayOfWeek, Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "de":
			switch (dayOfWeek) {
			case MONDAY: return "Mo";
			case TUESDAY: return "Di";
			case WEDNESDAY: return "Mi";
			case THURSDAY: return "Do";
			case FRIDAY: return "Fr";
			case SATURDAY: return "Sa";
			case SUNDAY: return "So";
			}
			break;
		case "es":
			switch (dayOfWeek) {
			case MONDAY: return "lu";
			case TUESDAY: return "ma";
			case WEDNESDAY: return "mi";
			case THURSDAY: return "ju";
			case FRIDAY: return "vi";
			case SATURDAY: return "sá";
			case SUNDAY: return "do";
			}
			break;
		case "fr":
			switch (dayOfWeek) {
			case MONDAY: return "lu";
			case TUESDAY: return "ma";
			case WEDNESDAY: return "me";
			case THURSDAY: return "je";
			case FRIDAY: return "ve";
			case SATURDAY: return "sa";
			case SUNDAY: return "di";
			}
			break;
		case "ja":
			switch (dayOfWeek) {
			case MONDAY: return "月";
			case TUESDAY: return "火";
			case WEDNESDAY: return "水";
			case THURSDAY: return "木";
			case FRIDAY: return "金";
			case SATURDAY: return "土";
			case SUNDAY: return "日";
			}
			break;
		case "ko":
			switch (dayOfWeek) {
			case MONDAY: return "월";
			case TUESDAY: return "화";
			case WEDNESDAY: return "수";
			case THURSDAY: return "목";
			case FRIDAY: return "금";
			case SATURDAY: return "토";
			case SUNDAY: return "일";
			}
			break;
		case "ru":
			switch (dayOfWeek) {
			case MONDAY: return "пн";
			case TUESDAY: return "вт";
			case WEDNESDAY: return "ср";
			case THURSDAY: return "чт";
			case FRIDAY: return "пт";
			case SATURDAY: return "сб";
			case SUNDAY: return "вс";
			}
			break;
		case "zh":
			switch (dayOfWeek) {
			case MONDAY: return "周一";
			case TUESDAY: return "周二";
			case WEDNESDAY: return "周三";
			case THURSDAY: return "周四";
			case FRIDAY: return "周五";
			case SATURDAY: return "周六";
			case SUNDAY: return "周日";
			}
			break;
		}
		// Fallback: English short form (first two letters of enum name)
		switch (dayOfWeek) {
		case MONDAY: return "Mo";
		case TUESDAY: return "Tu";
		case WEDNESDAY: return "We";
		case THURSDAY: return "Th";
		case FRIDAY: return "Fr";
		case SATURDAY: return "Sa";
		case SUNDAY: return "Su";
		}
		throw new IllegalArgumentException();
	}

	public static String shortName(Month month, Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "de":
			switch (month) {
			case JANUARY: return "Jan";
			case FEBRUARY: return "Feb";
			case MARCH: return "Mär";
			case APRIL: return "Apr";
			case MAY: return "Mai";
			case JUNE: return "Jun";
			case JULY: return "Jul";
			case AUGUST: return "Aug";
			case SEPTEMBER: return "Sep";
			case OCTOBER: return "Okt";
			case NOVEMBER: return "Nov";
			case DECEMBER: return "Dez";
			}
			break;
		case "es":
			switch (month) {
			case JANUARY: return "ene";
			case FEBRUARY: return "feb";
			case MARCH: return "mar";
			case APRIL: return "abr";
			case MAY: return "may";
			case JUNE: return "jun";
			case JULY: return "jul";
			case AUGUST: return "ago";
			case SEPTEMBER: return "sept";
			case OCTOBER: return "oct";
			case NOVEMBER: return "nov";
			case DECEMBER: return "dic";
			}
			break;
		case "fr":
			switch (month) {
			case JANUARY: return "janv.";
			case FEBRUARY: return "févr.";
			case MARCH: return "mars";
			case APRIL: return "avr.";
			case MAY: return "mai";
			case JUNE: return "juin";
			case JULY: return "juil.";
			case AUGUST: return "août";
			case SEPTEMBER: return "sept.";
			case OCTOBER: return "oct.";
			case NOVEMBER: return "nov.";
			case DECEMBER: return "déc.";
			}
			break;
		case "ja":
			switch (month) {
			case JANUARY: return "1月";
			case FEBRUARY: return "2月";
			case MARCH: return "3月";
			case APRIL: return "4月";
			case MAY: return "5月";
			case JUNE: return "6月";
			case JULY: return "7月";
			case AUGUST: return "8月";
			case SEPTEMBER: return "9月";
			case OCTOBER: return "10月";
			case NOVEMBER: return "11月";
			case DECEMBER: return "12月";
			}
			break;
		case "ko":
			switch (month) {
			case JANUARY: return "1월";
			case FEBRUARY: return "2월";
			case MARCH: return "3월";
			case APRIL: return "4월";
			case MAY: return "5월";
			case JUNE: return "6월";
			case JULY: return "7월";
			case AUGUST: return "8월";
			case SEPTEMBER: return "9월";
			case OCTOBER: return "10월";
			case NOVEMBER: return "11월";
			case DECEMBER: return "12월";
			}
			break;
		case "ru":
			switch (month) {
			case JANUARY: return "янв.";
			case FEBRUARY: return "февр.";
			case MARCH: return "март";
			case APRIL: return "апр.";
			case MAY: return "май";
			case JUNE: return "июнь";
			case JULY: return "июль";
			case AUGUST: return "авг.";
			case SEPTEMBER: return "сент.";
			case OCTOBER: return "окт.";
			case NOVEMBER: return "нояб.";
			case DECEMBER: return "дек.";
			}
			break;
		case "zh":
			switch (month) {
			case JANUARY: return "1月";
			case FEBRUARY: return "2月";
			case MARCH: return "3月";
			case APRIL: return "4月";
			case MAY: return "5月";
			case JUNE: return "6月";
			case JULY: return "7月";
			case AUGUST: return "8月";
			case SEPTEMBER: return "9月";
			case OCTOBER: return "10月";
			case NOVEMBER: return "11月";
			case DECEMBER: return "12月";
			}
			break;
		}
		// Fallback: English short form (first three letters of enum name)
		switch (month) {
		case JANUARY: return "Jan";
		case FEBRUARY: return "Feb";
		case MARCH: return "Mar";
		case APRIL: return "Apr";
		case MAY: return "May";
		case JUNE: return "Jun";
		case JULY: return "Jul";
		case AUGUST: return "Aug";
		case SEPTEMBER: return "Sep";
		case OCTOBER: return "Oct";
		case NOVEMBER: return "Nov";
		case DECEMBER: return "Dec";
		}
		throw new IllegalArgumentException();
	}

	public static String longName(Month month, Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "de":
			switch (month) {
			case JANUARY: return "Januar";
			case FEBRUARY: return "Februar";
			case MARCH: return "März";
			case APRIL: return "April";
			case MAY: return "Mai";
			case JUNE: return "Juni";
			case JULY: return "Juli";
			case AUGUST: return "August";
			case SEPTEMBER: return "September";
			case OCTOBER: return "Oktober";
			case NOVEMBER: return "November";
			case DECEMBER: return "Dezember";
			}
			break;
		case "es":
			switch (month) {
			case JANUARY: return "enero";
			case FEBRUARY: return "febrero";
			case MARCH: return "marzo";
			case APRIL: return "abril";
			case MAY: return "mayo";
			case JUNE: return "junio";
			case JULY: return "julio";
			case AUGUST: return "agosto";
			case SEPTEMBER: return "septiembre";
			case OCTOBER: return "octubre";
			case NOVEMBER: return "noviembre";
			case DECEMBER: return "diciembre";
			}
			break;
		case "fr":
			switch (month) {
			case JANUARY: return "janvier";
			case FEBRUARY: return "février";
			case MARCH: return "mars";
			case APRIL: return "avril";
			case MAY: return "mai";
			case JUNE: return "juin";
			case JULY: return "juillet";
			case AUGUST: return "août";
			case SEPTEMBER: return "septembre";
			case OCTOBER: return "octobre";
			case NOVEMBER: return "novembre";
			case DECEMBER: return "décembre";
			}
			break;
		case "ja":
			switch (month) {
			case JANUARY: return "1月";
			case FEBRUARY: return "2月";
			case MARCH: return "3月";
			case APRIL: return "4月";
			case MAY: return "5月";
			case JUNE: return "6月";
			case JULY: return "7月";
			case AUGUST: return "8月";
			case SEPTEMBER: return "9月";
			case OCTOBER: return "10月";
			case NOVEMBER: return "11月";
			case DECEMBER: return "12月";
			}
			break;
		case "ko":
			switch (month) {
			case JANUARY: return "1월";
			case FEBRUARY: return "2월";
			case MARCH: return "3월";
			case APRIL: return "4월";
			case MAY: return "5월";
			case JUNE: return "6월";
			case JULY: return "7월";
			case AUGUST: return "8월";
			case SEPTEMBER: return "9월";
			case OCTOBER: return "10월";
			case NOVEMBER: return "11월";
			case DECEMBER: return "12월";
			}
			break;
		case "ru":
			switch (month) {
			case JANUARY: return "январь";
			case FEBRUARY: return "февраль";
			case MARCH: return "март";
			case APRIL: return "апрель";
			case MAY: return "май";
			case JUNE: return "июнь";
			case JULY: return "июль";
			case AUGUST: return "август";
			case SEPTEMBER: return "сентябрь";
			case OCTOBER: return "октябрь";
			case NOVEMBER: return "ноябрь";
			case DECEMBER: return "декабрь";
			}
			break;
		case "zh":
			switch (month) {
			case JANUARY: return "一月";
			case FEBRUARY: return "二月";
			case MARCH: return "三月";
			case APRIL: return "四月";
			case MAY: return "五月";
			case JUNE: return "六月";
			case JULY: return "七月";
			case AUGUST: return "八月";
			case SEPTEMBER: return "九月";
			case OCTOBER: return "十月";
			case NOVEMBER: return "十一月";
			case DECEMBER: return "十二月";
			}
			break;
		}
		// Fallback: English long form
		switch (month) {
		case JANUARY: return "January";
		case FEBRUARY: return "February";
		case MARCH: return "March";
		case APRIL: return "April";
		case MAY: return "May";
		case JUNE: return "June";
		case JULY: return "July";
		case AUGUST: return "August";
		case SEPTEMBER: return "September";
		case OCTOBER: return "October";
		case NOVEMBER: return "November";
		case DECEMBER: return "December";
		}
		throw new IllegalArgumentException();
	}
}
