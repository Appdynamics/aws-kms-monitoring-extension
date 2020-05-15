/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.kms;

import com.appdynamics.extensions.aws.SingleNamespaceCloudwatchMonitor;
import com.appdynamics.extensions.aws.collectors.NamespaceMetricStatisticsCollector;
import com.appdynamics.extensions.aws.config.Configuration;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.aws.Constants.METRIC_PATH_SEPARATOR;

/**
 * Created by akshay.srivastava on 10/05/17.
 */
public class AWSKMSMonitor extends SingleNamespaceCloudwatchMonitor<Configuration> {

    private static final Logger LOGGER = ExtensionsLoggerFactory.getLogger(AWSKMSMonitor.class);

    private static final String DEFAULT_METRIC_PREFIX = String.format("%s%s%s%s",
            "Custom Metrics", METRIC_PATH_SEPARATOR, "Amazon KMS", METRIC_PATH_SEPARATOR);

    private static final String MONITOR_NAME = "AWSKMSMonitor";

    public AWSKMSMonitor() {
        super(Configuration.class);
        LOGGER.info(String.format("Using AWS KMS Monitor Version [%s]",
                this.getClass().getPackage().getImplementationTitle()));
    }

    @Override
    protected NamespaceMetricStatisticsCollector getNamespaceMetricsCollector(
            Configuration config) {
        MetricsProcessor metricsProcessor = createMetricsProcessor(config);

        return new NamespaceMetricStatisticsCollector
                .Builder(config.getAccounts(),
                config.getConcurrencyConfig(),
                config.getMetricsConfig(),
                metricsProcessor,
                config.getMetricPrefix())
                .withCredentialsDecryptionConfig(config.getCredentialsDecryptionConfig())
                .withProxyConfig(config.getProxyConfig())
                .build();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getDefaultMetricPrefix() {
        return  DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    @Override
    protected List<Map<String, ?>> getServers() {
        return new ArrayList<Map<String, ?>>();
    }

    private MetricsProcessor createMetricsProcessor(Configuration config) {
        return new AWSKMSMetricsProcessor(
                config.getMetricsConfig().getIncludeMetrics(),
                config.getDimensions());
    }

}