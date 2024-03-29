package uk.co.polycode.neo4j.rest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.math.BigInteger;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Organization
 *
 * An organization such as a school, NGO, corporation, club, etc.
 *
 *
 *             This file was generated by OWL to Java as a transformation of the Schema.org schema Version 14.0.
 *             Schema.org is released under the Creative Commons Attribution-ShareAlike License (version 3.0).
 *             The Schema.org license is applicable to the generated source files and the license is available from
 *             <a href="https://creativecommons.org/licenses/by-sa/3.0/">...</a>
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id") //, property="id"
public class Organization { // extends Thing {

	/**
	 * The synthetic key for this item.
	 */
	public UUID id;

	/**
	 * An alias for the item.
	 * (From Thing)
	 */
	public String alternateName;

	/**
	 * A description of the item.
	 * (From Thing)
	 */
	public String description;

	/**
	 * The identifier property represents any kind of identifier for any kind of <a class="localLink" href="https://schema.org/Thing">Thing</a>, such as ISBNs, GTIN codes, UUIDs etc. Schema.org provides dedicated properties for representing many of these, either as textual strings or as URL (URI) links. See <a href="/docs/datamodel.html#identifierBg">background notes</a> for more details.
	 * (From Thing)
	 */
	public String identifier;

	/**
	 * An image of the item. This can be a <a class="localLink" href="https://schema.org/URL">URL</a> or a fully described <a class="localLink" href="https://schema.org/ImageObject">ImageObject</a>.
	 * (From Thing)
	 */
	public String image;

	/**
	 * The name of the item.
	 * (From Thing)
	 */
	public String name;

	/**
	 * URL of a reference Web page that unambiguously indicates the item's identity. E.g. the URL of the item's Wikipedia page, Wikidata entry, or official website.
	 * (From Thing)
	 */
	public String sameAs;

	/**
	 * URL of the item.
	 * (From Thing)
	 */
	public URL url;

	/**
	 * Physical address of the item.
	 */
	public PostalAddress address;

	/**
	 * Alumni of an organization.
	 */
	public Person alumni;

	/**
	 * The geographic area where a service or offered item is provided.
	 */
	public Place areaServed;

	/**
	 * An award won by or for this item.
	 */
	public String award;

	/**
	 * The brand(s) associated with a product or service, or the brand(s) maintained by an organization or business person.
	 */
	public Organization brand;

	/**
	 * A relationship between an organization and a department of that organization, also described as an organization (allowing different urls, logos, opening hours). For example: a store with a pharmacy, or a bakery with a cafe.
	 */
	public Organization department;

	/**
	 * Email address.
	 */
	public String email;

	/**
	 * Someone working for this organization.
	 */
	public Person employee;

	/**
	 * The fax number.
	 */
	public String faxNumber;

	/**
	 * A person who founded this organization.
	 */
	public Person founder;

	/**
	 * The place where the Organization was founded.
	 */
	public Place foundingLocation;

	/**
	 * The <a href="http://www.gs1.org/gln">Global Location Number</a> (GLN, sometimes also referred to as International Location Number or ILN) of the respective organization, person, or place. The GLN is a 13-digit number used to identify parties and physical locations.
	 */
	public String globalLocationNumber;

	/**
	 * The International Standard of Industrial Classification of All Economic Activities (ISIC), Revision 4 code for a particular organization, business person, or place.
	 */
	public String isicV4;

	/**
	 * An organization identifier as defined in ISO 6523(-1). Note that many existing organization identifiers such as <a href="https://schema.org/leiCode">leiCode</a>, <a href="https://schema.org/duns">duns</a> and <a href="https://schema.org/vatID">vatID</a> can be expressed as an ISO 6523 identifier by setting the ICD part of the ISO 6523 identifier accordingly.
	 */
	public String iso6523Code;

	/**
	 * Keywords or tags used to describe some item. Multiple textual entries in a keywords list are typically delimited by commas, or by repeating the property.
	 */
	public String keywords;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a topic that is known about - suggesting possible expertise but not implying it. We do not distinguish skill levels here, or relate this to educational content, events, objectives or <a class="localLink" href="https://schema.org/JobPosting">JobPosting</a> descriptions.
	 * (Expanded subclass of Thing to literal association)
	 */
	public Person knowsAboutPerson;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a topic that is known about - suggesting possible expertise but not implying it. We do not distinguish skill levels here, or relate this to educational content, events, objectives or <a class="localLink" href="https://schema.org/JobPosting">JobPosting</a> descriptions.
	 * (Expanded subclass of Thing to literal association)
	 */
	public Organization knowsAboutOrganization;

	/**
	 * Of a <a class="localLink" href="https://schema.org/Person">Person</a>, and less typically of an <a class="localLink" href="https://schema.org/Organization">Organization</a>, to indicate a known language. We do not distinguish skill levels or reading/writing/speaking/signing here. Use language codes from the <a href="http://tools.ietf.org/html/bcp47">IETF BCP 47 standard</a>.
	 */
	public String knowsLanguage;

	/**
	 * The official name of the organization, e.g. the registered company name.
	 */
	public String legalName;

	/**
	 * The location of, for example, where an event is happening, where an organization is located, or where an action takes place.
	 */
	public Place locationPlace;

	/**
	 * The location of, for example, where an event is happening, where an organization is located, or where an action takes place.
	 */
	public PostalAddress location;

	/**
	 * An associated logo.
	 */
	public String logo;

	/**
	 * A member of an Organization or a ProgramMembership. Organizations can be members of organizations; ProgramMembership is typically for individuals.
	 */
	public List<Person> member = new ArrayList<>();

	/**
	 * An Organization to which this Person or Organization belongs.
	 */
	public Organization memberOf;

	/**
	 * The North American Industry Classification System (NAICS) code for a particular organization or business person.
	 */
	public String naics;

	/**
	 * The number of employees in an organization e.g. business.
	 */
	public BigInteger numberOfEmployees;

	/**
	 * The larger organization that this organization is a <a class="localLink" href="https://schema.org/subOrganization">subOrganization</a> of, if any.
	 */
	public Organization parentOrganization;

	/**
	 * A relationship between two organizations where the first includes the second, e.g., as a subsidiary. See also: the more specific 'department' property.
	 */
	public Organization subOrganization;

	/**
	 * The Tax / Fiscal ID of the organization or person, e.g. the TIN in the US or the CIF/NIF in Spain.
	 */
	public String taxID;

	/**
	 * The telephone number.
	 */
	public String telephone;

	/**
	 * The Value-added Tax ID of the organization or person.
	 */
	public String vatID;

	/**
	 * The date that this organization was dissolved.
	 */
	public ZonedDateTime dissolutionDate;

	/**
	 * The date that this organization was founded.
	 */
	public ZonedDateTime foundingDate;

	/**
	 * Where to find the definition of the OWL Class used to generate this Java class.
	 */
	public static String isDefinedBy = "https://schema.org/Organization";
}
