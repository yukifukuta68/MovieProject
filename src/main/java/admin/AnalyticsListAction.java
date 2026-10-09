
package admin;

import java.time.LocalDate;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.AnalyticsDao;
import tool.Action;

public class AnalyticsListAction extends Action {

    @Override
    public void execute(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // 分析対象日は今日
        LocalDate analysisDate = LocalDate.now();

        AnalyticsDao dao = new AnalyticsDao();

        // 座席の予約状況を集計
        Map<String, Object> result =
                dao.getSeatAnalysis(analysisDate);

        // JSPで使用するデータを設定
        request.setAttribute("analysisDate", analysisDate);
        request.setAttribute("scheduleCount", result.get("scheduleCount"));
        request.setAttribute("totalSeats", result.get("totalSeats"));
        request.setAttribute("reservedSeats", result.get("reservedSeats"));
        request.setAttribute("areaStats", result.get("areaStats"));
        request.setAttribute("rowStats", result.get("rowStats"));
        request.setAttribute("topArea", result.get("topArea"));
        request.setAttribute("topRow", result.get("topRow"));

        // JSPへ遷移
        request.getRequestDispatcher(
                "/admin/analysis/index.jsp")
                .forward(request, response);
    }
}