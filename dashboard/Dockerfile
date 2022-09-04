FROM jamesread/olivetin

MAINTAINER Polycode Limited https://polycode.co.uk/

# SSH credentials for inter-container communication \
USER olivetin
COPY --chown=olivetin:olivetin ./ssh/id_rsa_dashboard.pub /home/olivetin/id_rsa_dashboard.pub
COPY --chown=olivetin:olivetin ./ssh/id_rsa_dashboard     /home/olivetin/id_rsa_dashboard
RUN \
	 mkdir -p /home/olivetin/.ssh \
	 && mv /home/olivetin/id_rsa_dashboard.pub ~/.ssh/. \
    && mv /home/olivetin/id_rsa_dashboard     ~/.ssh/. \
	 && chmod u+rwx,go-rwx /home/olivetin/.ssh \
	 && chmod u+rw,go+r-wx /home/olivetin/.ssh/id_rsa_dashboard.pub \
	 && chmod u+rw,go-rwx  /home/olivetin/.ssh/id_rsa_dashboard \
	 ;

#	 && chmod u+rwx,go-rwx /home/olivetin/.ssh \
#	 && chmod u+rw,go+r-wx /home/olivetin/.ssh/id_rsa_dashboard.pub \
#	 && chmod u+rw,go-rwx  /home/olivetin/.ssh/id_rsa_dashboard \
#     && chown -R olivetin:olivetin /home/olivetin/.ssh \
