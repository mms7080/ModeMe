package com.example.Modeme.purchase.controller;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentApiController {

//	private IamportClient iamportClient;
//
//	@Autowired
//	private PaymentService pService;
//
//	@Autowired
//	private RefundService rService;
//
//	@Value("${imp.api.key}")
//	private String apikey;
//
//	@Value("${imp.api.secretkey}")
//	private String secretkey;
//
//	@PostConstruct
//	public void init() {
//		this.iamportClient = new IamportClient(apikey, secretkey);
//	}
//
//	// 결제시 결제내역이 저장되는 메소드
//	@PostMapping("payment.pm")
//	@ResponseBody
//	public String paymentComplete(HttpSession session, @RequestParam("impUid") String impUid,
//			@RequestParam("merchantUid") String merchantUid, @RequestParam("poNo") int pointNo,
//			@RequestParam("point") int point) {
//
//			User loginUser = (User) session.getAttribute("loginUser");
//
//			Payment payments = new Payment();
//			payments.setMemberNo(loginUser.getMemberNo());
//			payments.setPointNo(pointNo);
//			payments.setImpUid(impUid);
//			payments.setMerchantUid(merchantUid);
//			int sResult = pService.saveOrder(payments);
//
//			User m = new User();
//			m.setMemberNo(loginUser.getMemberNo());
//			m.setMemberPoint(point);
//			int uResult = pService.updateMemberPoint(m);
//			
//			loginUser.setMemberPoint(loginUser.getMemberPoint() + point);
//			
//			session.setAttribute("loginUser", loginUser);
//			return sResult + uResult == 2 ? "success" : "fail";
//
//	}
//
//	@PostMapping("canclePay.pm")
//	@ResponseBody
//	public String refundComplete(HttpSession session,
//			@RequestParam("merchantUidList") ArrayList<String> merchantUidList,
//			@RequestParam("cancleAmount") int cancleAmount, @RequestParam("cancelPoint") int cancelPoint,
//			@RequestParam("cancelReason") String cancelReason,
//			@RequestParam("payNoList")ArrayList<Integer> payNoList)throws IOException {
//		
//		User loginUser = (User) session.getAttribute("loginUser");
//		int result = 0;
//		if (loginUser.getMemberPoint() >= cancelPoint) {
//			for(int i = 0 ; i < payNoList.size(); i++) {
//				//취소 회원 번호, 취소 사유, 취소 구분 타입,
//				Cancel cancel = new Cancel();
//				cancel.setCancelMemNo(loginUser.getMemberNo());
//				cancel.setCancelComent(cancelReason);
//				cancel.setCancelRefNo(payNoList.get(i));
//				cancel.setCancelRefType("PAYMENT");
//				pService.insertCancelReason(cancel);
//			}
//			
//			for (int i = 0; i < merchantUidList.size(); i++) {
//				String token = rService.getToken(apikey, secretkey);
//				rService.refundRequest(token, merchantUidList.get(i));
//				result = pService.deletePayments(merchantUidList.get(i));
//				
//				
//			}
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("memberNo", loginUser.getMemberNo());
//			map.put("canclePoint", cancelPoint);
//			int result2 = pService.minusPoint(map);
//			
//			loginUser.setMemberPoint(loginUser.getMemberPoint()-cancelPoint);
//			session.setAttribute("loginUser", loginUser);
//			return result + result2 >= 2 ? "success" : "fail";
//		}else {
//			return "shortage";
//		}
//	}
}