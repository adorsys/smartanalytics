# Smartanalytics API

# Licensing model change to dual license: _AGPL v.3_ or _commercial license_

**Attention: this open-source project will change its licensing model as of _01.01.2022_!**

Constantly evolving and extending scope, production traffic and support in open banking world call for high maintenance and service investments on our part.

Henceforth, adorsys will offer all versions higher than v1.0 of Open Banking Gateway under a dual-license model. 
Thus, this repository will be available either under Affero GNU General Public License v.3 (AGPL v.3) or alternatively under a commercial license agreement.

We would like to thank all our users for their trust so far and are convinced that we will be able to provide an even better service going forward.

For more information, advice for your implementation project or if your use case requires more time to adapt this change, 
please contact us at [psd2@adorsys.com](mailto:psd2@adorsys.com).

For additional details please see the section [FAQ on Licensing Change](#faq-on-licensing-change).


## Release process

1. Assure that you have rights to push to develop and master branches
2. Check the build and Javadocs locally:
```bash
mvn clean install javadoc:javadoc
```
3. Setup release scripts for your local copy
```bash
git submodule update
```
or
```bash
git submodule init
```
4. Make a release from develop branch by following command:
```
scripts/release-scripts/release.sh 0.0.1 0.0.2
```
Where:  
-- 0.0.1 - a version to release  
-- 0.0.2 - a next version to develop
5. Follow release scripts commands, including push


More details see in release-scripts: https://github.com/borisskert/release-scripts
