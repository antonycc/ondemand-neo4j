package uk.co.polycode.neo4j

import com.github.victools.jsonschema.generator.Option
import com.github.victools.jsonschema.generator.OptionPreset
import com.github.victools.jsonschema.generator.SchemaGenerator
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder
import com.github.victools.jsonschema.generator.SchemaVersion

/**
 * On-demand Neo4j is an exploration of Neo4j with deployment to AWS
 * Copyright (C) 2022  Antony Cartwright, Polycode Limited
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0 for more details.
 */
open class JavaToJsonSchema(private var javaModels: List<Class<*>>) {

    private val schemaGeneratorConfigBuilder =
        SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
            .with(Option.SCHEMA_VERSION_INDICATOR) // OptionPreset.PLAIN_JSON
            .with(Option.ADDITIONAL_FIXED_TYPES) // OptionPreset.PLAIN_JSON
            .with(Option.EXTRA_OPEN_API_FORMAT_VALUES) // OptionPreset.PLAIN_JSON
            .with(Option.FLATTENED_ENUMS) // OptionPreset.PLAIN_JSON
            .with(Option.FLATTENED_OPTIONALS) // OptionPreset.PLAIN_JSON
            .with(Option.FLATTENED_SUPPLIERS) // OptionPreset.PLAIN_JSON
            .with(Option.VALUES_FROM_CONSTANT_FIELDS) // OptionPreset.PLAIN_JSON
            //.with(Option.PUBLIC_STATIC_FIELDS)
            .with(Option.PUBLIC_NONSTATIC_FIELDS) // OptionPreset.PLAIN_JSON
            .without(Option.NONPUBLIC_NONSTATIC_FIELDS_WITH_GETTERS) // OptionPreset.PLAIN_JSON
            .without(Option.NONPUBLIC_NONSTATIC_FIELDS_WITHOUT_GETTERS) // OptionPreset.PLAIN_JSON
            //.with(Option.NULLABLE_FIELDS_BY_DEFAULT)
            //.with(Option.PLAIN_DEFINITION_KEYS)
            .with(Option.ALLOF_CLEANUP_AT_THE_END) // OptionPreset.PLAIN_JSON

    fun toJsonSchema(): String {
        val generator = SchemaGenerator(schemaGeneratorConfigBuilder.build())
        val schemaBuilder = generator.buildMultipleSchemaDefinitions()
        javaModels.asSequence().forEach { schemaBuilder.createSchemaReference(it) }
        val jsonSchemaNode = schemaBuilder.collectDefinitions("definitions")
        return jsonSchemaNode?.toPrettyString() ?: "The Generated jsonSchema was null."
    }
}
