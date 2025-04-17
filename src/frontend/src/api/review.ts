import { http } from "@/utils/http";

/**
 * 提交人工审核申请
 * @param reportId 报告ID
 * @param reviewData 审核申请数据
 * @returns 提交结果
 */
export const submitReviewRequest = (reportId: string | number, reviewData: {
  reason: string;
  contactInfo?: string;
  priority: 'low' | 'normal' | 'high';
}) => {
  return http.post<{ code: string; message: string; data: any }, { reportId: string | number; reason: string; contactInfo?: string; priority: 'low' | 'normal' | 'high' }>(
    "/api/report/review-request",
    {
      data: {
        reportId,
        ...reviewData
      }
    },
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );
};
