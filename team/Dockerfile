ARG BASE_IMAGE=dbeaver/cloudbeaver-tm

FROM ${BASE_IMAGE}

ARG PRODUCT

ARG JDK

ARG TARGETPLATFORM

COPY --chown=root:root ${JDK}/${TARGETPLATFORM} /opt/java/openjdk

COPY --chown=root:root ./dbeaver-agent.jar /opt/${PRODUCT}/server/dbeaver-agent.jar

RUN sed -i 's|exec java ${JAVA_OPTS}|exec java -javaagent:server/dbeaver-agent.jar ${JAVA_OPTS}|' /opt/${PRODUCT}/run-cloudbeaver-server.sh
