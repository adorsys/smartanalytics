# Smartanalytics API

# Licensing model change to dual license: _AGPL v.3_ or _commercial license_

**Attention: this open-source project will change its licensing model as of _01.01.2022_!**

Constantly evolving and extending scope, production traffic and support in open banking world call for high maintenance 
and service investments on our part.

Henceforth, adorsys will offer all versions higher v2.3.10 of Smart Analytics under a dual-license model. 
Thus, this repository will be available either under Affero GNU General Public License v.3 (AGPL v.3) or alternatively 
under a commercial license agreement.

We would like to thank all our users for their trust so far and are convinced that we will be able to provide an even 
better service going forward.

For more information, advice for your implementation project or if your use case requires more time to adapt this change, 
please contact us at [sales@adorsys.com](mailto:sales@adorsys.com).

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


# FAQ on Licensing Change

## What is a dual-licensing model?
Under a dual-licensing model, our product is available under two licenses
- [The Affero GNU General Public License v3 (AGPL v3)](https://www.gnu.org/licenses/agpl-3.0.en.html)
- A proprietary commercial license

If you are a developer or business that would like to review our products in detail, test and implement in your
open-source projects and share the changes back to the community, the product repository is freely available under AGPL v3.

If you are a business that would like to implement our products in a commercial setting and would like to protect your
individual changes, we offer the option to license our products under a commercial license.

This change will still allow free access and ensure openness under AGPL v3 but with assurance of committing any
alterations or extensions back to the project and preventing redistribution of such implementations under commercial license.

## Will there be any differences between the open-source and commercially licensed versions of your products?
Our public release frequency will be reduced as our focus shifts towards the continuous maintenance of the commercial version.
Nevertheless, we are committed to also provide open-source releases of our products on a regular basis as per our release policy.

For customers with a commercial license, we will offer new intermediate releases in a more frequent pace.

## Does this mean that this product is no longer open source?
No, the product will still be published and available on GitHub under an OSI-approved open-source license (AGPL v3).

## What about adorsys’ commitment to open source? Will adorsys provide future product releases on GitHub?
We at adorsys are committed to continue actively participating in the open-source community. Our products remain
licensed under OSI-approved open-source licenses, and we are looking forward to expanding our product portfolio on GitHub even further.

## How does the change impact me if I already use the open-source edition of your product?
All currently published versions until v1.0.0.1 will remain under their current Apache 2.0 license and its respective
requirements and you may continue using it as-is. To upgrade to future versions, you will be required to either abide
by the requirements of AGPL v3, including documenting and sharing your implemented changes to the product when distributing,
or alternatively approach us to obtain a commercial license.

## What if I cannot adjust to the new licensing model until 01.01.2022? Can I extend the deadline?
We understand that adjustment to licensing changes can take time and therefore are open to discuss extension options
on an individual basis. For inquiries please contact us as [psd2@adorsys.com](psd2:sales@adorsys.com).

## Which versions of the product are affected?
All versions of Datasafe after v1.0.0.1 will be affected by the licensing changes and move to a dual-licensing model.

## What will happen to older, Apache 2.0 licensed product versions?
All older Apache 2.0 licensed versions prior and including v1.0.0.1 will remain available under their existing license.

## What open-source products from Adorsys are affected by the licensing change?
The following products are affected:
- [XS2A Core](https://github.com/adorsys/xs2a)
- [XS2A Sandbox & ModelBank](https://github.com/adorsys/XS2A-Sandbox)
- [Open Banking Gateway](https://github.com/adorsys/open-banking-gateway) incl. [XS2A Adapters](https://github.com/adorsys/xs2a-adapter)
- [SmartAnalytics](https://github.com/adorsys/smartanalytics)
- [Datasafe](https://github.com/adorsys/datasafe)

## I’m using one of these products indirectly via some software integrator. How does the licensing change affect me?
The licensing change does not affect you as user, but it is relevant to your provider who has used our product in their
solution implementation. In case of uncertainty please contact your service provider or approach us at [sales@adorsys.com](mailto:sales@adorsys.com).
