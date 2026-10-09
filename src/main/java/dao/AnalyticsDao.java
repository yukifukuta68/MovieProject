
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsDao extends Dao {

    // 仮の座席配置：A～J列、各列10席
    private static final int SEATS_PER_ROW = 10;
    private static final int ROW_COUNT = 10;
    private static final int TOTAL_SEATS_PER_SCREENING =
            SEATS_PER_ROW * ROW_COUNT;

    /**
     * エリア別・列別の座席予約状況を取得する。
     */
    public Map<String, Object> getSeatAnalysis(
            LocalDate analysisDate) throws Exception {

        Map<String, Object> result = new LinkedHashMap<>();

        // エリア別の予約数
        Map<String, Integer> areaReserved = new LinkedHashMap<>();

        areaReserved.put("前方", 0);
        areaReserved.put("中央", 0);
        areaReserved.put("後方", 0);

        // 列別の予約数
        Map<String, Integer> rowReserved = new LinkedHashMap<>();

        for (char row = 'A'; row <= 'J'; row++) {
            rowReserved.put(String.valueOf(row), 0);
        }

        Timestamp start = Timestamp.valueOf(
                analysisDate.atStartOfDay());

        Timestamp end = Timestamp.valueOf(
                analysisDate.plusDays(1).atStartOfDay());

        int scheduleCount = 0;
        int reservedSeats = 0;

        try (Connection con = getConnection()) {

            // ① 今日の上映回数を取得
            String scheduleSql =
                    "SELECT COUNT(*) AS SCHEDULE_COUNT "
                    + "FROM SCREENING_SCHEDULES "
                    + "WHERE START_DATETIME >= ? "
                    + "AND START_DATETIME < ?";

            try (PreparedStatement st =
                    con.prepareStatement(scheduleSql)) {

                st.setTimestamp(1, start);
                st.setTimestamp(2, end);

                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        scheduleCount =
                                rs.getInt("SCHEDULE_COUNT");
                    }
                }
            }

            // ② 今日の上映回における予約済み座席を取得
            String seatSql =
                    "SELECT seat.SEAT_NO "
                    + "FROM SCREENING_SCHEDULES s "
                    + "JOIN RESERVATIONS r "
                    + "ON r.SCHEDULE_ID = s.SCHEDULE_ID "
                    + "JOIN RESERVATION_SEATS seat "
                    + "ON seat.RESERVATION_ID = r.RESERVATION_ID "
                    + "AND seat.SCHEDULE_ID = r.SCHEDULE_ID "
                    + "WHERE s.START_DATETIME >= ? "
                    + "AND s.START_DATETIME < ? "
                    + "AND r.STATUS = ?";

            try (PreparedStatement st =
                    con.prepareStatement(seatSql)) {

                st.setTimestamp(1, start);
                st.setTimestamp(2, end);
                st.setString(3, ReservationDao.STATUS_RESERVED);

                try (ResultSet rs = st.executeQuery()) {

                    while (rs.next()) {

                        String seatNo = rs.getString("SEAT_NO");

                        // A1、B3などの座席番号を解析
                        if (seatNo == null
                                || !seatNo.matches(
                                        "^[A-J](10|[1-9])$")) {
                            continue;
                        }

                        char rowChar = seatNo.charAt(0);
                        String rowName =
                                String.valueOf(rowChar);

                        // 列別の予約数を加算
                        rowReserved.put(
                                rowName,
                                rowReserved.get(rowName) + 1);

                        // エリア別の予約数を加算
                        String areaName =
                                getAreaName(rowChar);

                        areaReserved.put(
                                areaName,
                                areaReserved.get(areaName) + 1);

                        reservedSeats++;
                    }
                }
            }
        }

        // ③ 全体の座席数
        int totalSeats =
                scheduleCount * TOTAL_SEATS_PER_SCREENING;

        // ④ エリア別集計
        List<Map<String, Object>> areaStats =
                new ArrayList<>();

        String[][] areas = {
            {"前方", "A-C", "A", "C"},
            {"中央", "D-G", "D", "G"},
            {"後方", "H-J", "H", "J"}
        };

        Map<String, Object> topArea = null;
        double maxAreaRate = -1;

        for (String[] area : areas) {

            String name = area[0];
            String rowsLabel = area[1];

            int firstRow = area[2].charAt(0);
            int lastRow = area[3].charAt(0);
            int rowCount = lastRow - firstRow + 1;

            int areaTotal =
                    rowCount * SEATS_PER_ROW * scheduleCount;

            int areaReservedSeats = areaReserved.get(name);

            Map<String, Object> item =
                    new LinkedHashMap<>();

            item.put("name", name);
            item.put("rowsLabel", rowsLabel);
            item.put("total", areaTotal);
            item.put("reserved", areaReservedSeats);

            areaStats.add(item);

            double rate = areaTotal > 0
                    ? areaReservedSeats * 100.0 / areaTotal
                    : 0;

            if (areaTotal > 0 && rate > maxAreaRate) {
                maxAreaRate = rate;
                topArea = item;
            }
        }

        // ⑤ 列別集計
        List<Map<String, Object>> rowStats =
                new ArrayList<>();

        Map<String, Object> topRow = null;
        double maxRowRate = -1;

        for (char row = 'A'; row <= 'J'; row++) {

            String name = String.valueOf(row);

            int rowTotal =
                    SEATS_PER_ROW * scheduleCount;

            int rowReservedCount = rowReserved.get(name);

            Map<String, Object> item =
                    new LinkedHashMap<>();

            item.put("name", name);
            item.put("areaName", getAreaName(row));
            item.put("total", rowTotal);
            item.put("reserved", rowReservedCount);

            rowStats.add(item);

            double rate = rowTotal > 0
                    ? rowReservedCount * 100.0 / rowTotal
                    : 0;

            if (rowTotal > 0 && rate > maxRowRate) {
                maxRowRate = rate;
                topRow = item;
            }
        }

        // ⑥ JSPへ渡す集計結果
        result.put("scheduleCount", scheduleCount);
        result.put("totalSeats", totalSeats);
        result.put("reservedSeats", reservedSeats);
        result.put("areaStats", areaStats);
        result.put("rowStats", rowStats);
        result.put("topArea", topArea);
        result.put("topRow", topRow);

        return result;
    }

    /**
     * 列番号からエリア名を取得する。
     */
    private String getAreaName(char row) {

        if (row >= 'A' && row <= 'C') {
            return "前方";
        }

        if (row >= 'D' && row <= 'G') {
            return "中央";
        }

        return "後方";
    }
}