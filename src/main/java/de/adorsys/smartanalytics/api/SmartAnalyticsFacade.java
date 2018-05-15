package de.adorsys.smartanalytics.api;

/**
 * Provides access to an analytics service.
 *
 * This can be implemented by a remote connector to consume a remote smart analytic server or
 * by an embedded connector to consume an embedded module.
 *
 * @author fpo 2018-05-06 04:58
 *
 */
public interface SmartAnalyticsFacade {

	AnalyticsResult analyzeBookings(AnalyticsRequest analyticsRequest);

}
