package com.teamdev.service;

import java.io.File;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teamdev.service.dto.ReportingPrinter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JasperManagerService {

	public String user = "root";
	public String password = "gims1703";
	public String port = "3306";
	public String server = "localhost";
	public String database = "JasperTest";
	public String classForName = "com.mysql.jdbc.Driver";

	private String url = "";

	public java.sql.Connection con = null;

	private final String REPERTOIRE_JASPER = "jaspers/";

	public static final String DOWNLOAD_DIR = "src/main/webapp/downloadDocs/";


	private final String PERSONNES = "personnes.jasper";

	@Autowired
	PersonnesService personnesService;

	public Map<String, Object> parameters = new HashMap<String, Object>();


	void addInfo(Map<String, Object> parameters) {

		parameters.clear();
		parameters.put("powered", "Powered by TEAMDEV SOLUTION");

		String chemin = new File(REPERTOIRE_JASPER).getAbsolutePath();

		parameters.put("Path", chemin + "/");
	}


	public ReportingPrinter printPersonnes() {

		ReportingPrinter r = new ReportingPrinter();
		try {

				con = getConnection();

				addInfo(parameters);

				parameters.put("title", "LISTE DES PERSONNES");

				String fileName = "personnes_" + Math.random() +".pdf";
				String FlowFilePath = new File(DOWNLOAD_DIR, fileName)
						.getAbsolutePath();
				String path = REPERTOIRE_JASPER + PERSONNES;
				JasperReport jasperReport = null;
				jasperReport = (JasperReport) JRLoader.loadObjectFromFile(path);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, con);
				JasperExportManager.exportReportToPdfFile(jasperPrint,
						FlowFilePath);
				r.setDirectorie(fileName);
				r.setDirectorieLong(FlowFilePath);

				r.setMessage("Impression de la liste des personnes.");

		} catch (Exception ex) {
			Logger.getLogger(JasperManagerService.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return r;
	}



	/**
	 *
	 * @return
	 * @throws Exception
	 */
	private java.sql.Connection getConnection() throws Exception {
		try {

			Class.forName(classForName);
			url = "jdbc:mysql://" + server + ":" + port + "/" + database
					+ "?useUnicode=true&characterEncoding=utf8&useSSL=false";
			return DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			throw new Exception(
					"Impossible d'etablir la connexion avec la base de donnée");
		}

	}

}
