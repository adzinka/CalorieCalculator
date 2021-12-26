package com.example.caloriecalculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.caloriecalculator.converters.Converters
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.model.Ration

@Database(entities = [Product::class, Ration::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun rationDao(): RationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS " +
                        "'ration_temporary' ('id' INTEGER NOT NULL, 'product_id' INTEGER NOT NULL," +
                        "'weight' FLOAT NOT NULL, 'created_at' INTEGER NOT NULL, PRIMARY KEY('id'))")
                database.execSQL("INSERT INTO ration_temporary(id, product_id, weight, created_at) SELECT id, product_id, weight, created_at FROM ration")
                database.execSQL("DROP TABLE ration")
                database.execSQL("ALTER TABLE ration_temporary RENAME TO ration")
            }
        }

        private val MIGRATION_2_3: Migration = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE product "
                        + " ADD COLUMN isVisible INTEGER NOT NULL DEFAULT(0)");
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                    )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}