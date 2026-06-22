@param ru.mentee.power.crm.model.Lead lead

@template.layout.main(
content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Редактирование лида</h1>

        <form action="/leads/${lead.id().toString()}" method="post" class="space-y-4">
            <input type="hidden" name="id" value="${lead.id().toString()}"/>
            <!-- Email -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                    Email
                </label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        value="${lead.email()}"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="example@company.com"
                />
            </div>
            <!-- Telephone -->
            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
                    Telephone
                </label>
                <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value="${lead.phone()}"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="+7 922 222 33 55"
                />
            </div>
            <!-- Company -->
            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-1">
                    Company
                </label>
                <input
                        type="text"
                        id="company"
                        name="company"
                        value="${lead.company()}"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="example org"
                />
            </div>
            <!-- Select for status -->
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">
                    Status
                </label>
                <select
                        id="status"
                        name="status"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500">
                    @if(lead.status() == ru.mentee.power.crm.model.LeadStatus.NEW)
                        <option value="NEW" selected>Новый лид</option>
                    @else
                        <option value="NEW">Новый лид</option>
                    @endif
                    @if(lead.status() == ru.mentee.power.crm.model.LeadStatus.CONTACTED)
                        <option value="CONTACTED" selected>Связались</option>
                    @else
                        <option value="CONTACTED">Связались</option>
                    @endif
                    @if(lead.status() == ru.mentee.power.crm.model.LeadStatus.QUALIFIED)
                        <option value="QUALIFIED" selected>Целевой</option>
                    @else
                        <option value="QUALIFIED">Целевой</option>
                    @endif
                    @if(lead.status() == ru.mentee.power.crm.model.LeadStatus.CONVERTED)
                        <option value="CONVERTED" selected>Закрыт (успешно)</option>
                    @else
                        <option value="CONVERTED">Закрыт (успешно)</option>
                    @endif
                </select>
            </div>
            <!-- Submit Button -->
            <button
                    type="submit"
                    class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500"
            >
                Обновить информацию
            </button>

            <!-- Cancel Link -->
            <a
                    href="/leads"
                    class="block text-center text-sm text-gray-600 hover:text-gray-900"
            >
                Отмена
            </a>
        </form>
    </div>
`
)