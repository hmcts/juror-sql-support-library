 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.4.18
FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/juror-sql-support-library.jar /opt/app/

EXPOSE 8080
CMD [ "juror-sql-support-library.jar" ]
