package com.github.freeacs.web.app.page.report.custom;

import com.github.freeacs.dbi.ACS;
import com.github.freeacs.dbi.Group;
import com.github.freeacs.dbi.Profile;
import com.github.freeacs.dbi.Unittype;
import com.github.freeacs.dbi.report.*;
import com.github.freeacs.web.app.input.ParameterParser;
import com.github.freeacs.web.app.page.report.ReportData;
import com.github.freeacs.web.app.util.ACSLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The Class VoipInterface.
 */
public class ProvRetriever extends ReportRetriever {

	/** The generator */
	private ReportProvisioningGenerator generatorProv;

	/**
	 * Instantiates a new voip interface.
	 *
	 * @param inputData the input data
	 * @param params the params
	 * @param acs the xaps
	 * @throws SQLException the sQL exception
	 *  the no available connection exception
	 */
	public ProvRetriever(ReportData inputData, ParameterParser params, ACS acs) throws SQLException {
		super(inputData, params, acs);
		generatorProv = new ReportProvisioningGenerator(acs.getDataSource(), acs.getSyslog().getDataSource(), acs,
				null, ACSLoader.getIdentity(params.getSession().getId(), acs.getDataSource()));
	}

	/* (non-Javadoc)
	 * @see com.owera.xaps.web.app.page.report.custom.ReportRetriever#generateReport(com.owera.xaps.dbi.report.PeriodType, java.util.Date, java.util.Date, java.util.List, java.util.List)
	 */
	@Override
	public Report<?> generateReport(PeriodType periodType, Date start, Date end, List<Unittype> unittypes, List<Profile> profiles, Group groupSelect) throws SQLException, IOException {
		Report<?> report = null;
		if (getInputData().getRealtime().getBoolean() || groupSelect != null) {
			report = (Report<RecordProvisioning>) generatorProv.generateFromSyslog(periodType, start, end, unittypes, profiles, null, groupSelect);
		} else
			report = (Report<RecordProvisioning>) generatorProv.generateFromReport(periodType, start, end, unittypes, profiles);
		return report;
	}

	/* (non-Javadoc)
	 * @see com.owera.xaps.web.app.page.report.custom.ReportRetriever#applyObjects(java.util.Map)
	 */
	@Override
	public void applyObjects(Map<String, Object> root) {

	}

	@Override
	public ReportGenerator getReportGenerator() {
		return generatorProv;
	}

}
