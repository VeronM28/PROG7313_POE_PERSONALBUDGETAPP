package com.st10083866.prog7313_poe_personalbudgetapp.database

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.*

@Database(
    entities =  [User::class, Transaction::class, Budget::class, Category::class, SavingsGoal::class, SavingsContribution::class, HelpPageContent::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun savingsContributionDao(): SavingsContributionDao
    abstract fun helpPageContentDao(): HelpPageContentDao

    companion object{
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return Instance?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_app_dp").build()
                Instance = instance
                instance
            }
        }
    }
}

