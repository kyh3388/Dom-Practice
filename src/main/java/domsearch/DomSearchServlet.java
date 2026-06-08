package domsearch;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/domSearch")
public class DomSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			
			String theme = getParameterValue(request, "theme");
			String searchId = getParameterValue(request, "searchId");
			String searchNodeName = getParameterValue(request, "searchNodeName");
			String searchAttrName = getParameterValue(request, "searchAttrName");
			String searchAttrValue = getParameterValue(request, "searchAttrValue");
			
			String targetFileName;
			
			if ("black".equals(theme)) {
				targetFileName = "/black_dom.html";
			} else {
				targetFileName = "/white_dom.html";
			}
			
			String filePath = getServletContext().getRealPath(targetFileName);
			File file = new File(filePath);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(file);
			Node rootNode = document.getDocumentElement();
			
			String idResult = "";
			String nodeNameResult = "";
			String attrNameResult = "";
			String attrValueResult = "";
			
			if (!"".equals(searchId)) {
				String result = DomSearch.searchIdNode(rootNode, searchId);
				
				if (result == null) {
					idResult = "검색 결과 없음 / 0개";
				} else {
					idResult = "검색 성공: " + result + "(Java)";
				}
			}
			
			if (!"".equals(searchNodeName)) {
				int count = DomSearch.countNodeName(rootNode, searchNodeName);
				nodeNameResult = searchNodeName + " 노드 개수: " + count + "개(Java)";
			}

			if (!"".equals(searchAttrName)) {
				int count = DomSearch.countAttrName(rootNode, searchAttrName);
				attrNameResult = searchAttrName + " 속성 개수: " + count + "개(Java)";
			}

			if (!"".equals(searchAttrValue)) {
				int count = DomSearch.countAttrValue(rootNode, searchAttrValue);
				attrValueResult = searchAttrValue + " 속성값 개수: " + count + "개(Java)";
			}
			
			String json = "{"
					+ "\"idResult\":\"" + escapeJson(idResult) + "\","
					+ "\"nodeNameResult\":\"" + escapeJson(nodeNameResult) + "\","
					+ "\"attrNameResult\":\"" + escapeJson(attrNameResult) + "\","
					+ "\"attrValueResult\":\"" + escapeJson(attrValueResult) + "\""
					+ "}";

			response.getWriter().write(json);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Java 검색 처리 중 오류가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}

	private String getParameterValue(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);

		if (value == null) {
			return "";
		}

		return value.trim();
	}

	private String escapeJson(String value) {
		if (value == null) {
			return "";
		}

		return value
				.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\r", "\\r")
				.replace("\n", "\\n");
	}
}